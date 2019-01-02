package com.example.cpu11398_local.etalk.domain.interactor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.annotation.RequiresApi;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import com.example.cpu11398_local.etalk.data.repository.ConversationRepository;
import com.example.cpu11398_local.etalk.data.repository.UserRepository;
import com.example.cpu11398_local.etalk.presentation.model.Conversation;
import com.example.cpu11398_local.etalk.presentation.model.Message;
import com.example.cpu11398_local.etalk.presentation.model.User;
import com.example.cpu11398_local.etalk.presentation.view.chat.group.MessageGroupItem;
import com.example.cpu11398_local.etalk.presentation.view_model.ViewModelCallback;
import com.example.cpu11398_local.etalk.utils.Event;
import com.example.cpu11398_local.etalk.utils.Tool;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.vincent.videocompressor.VideoCompress;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import javax.inject.Inject;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class ChatGroupUsecase implements Usecase {

    private final String SENT_URL           = "https://firebasestorage.googleapis.com/v0/b/etalk-180808.appspot.com/o/status%2Fsent.png?alt=media&token=06a4cf9e-b36d-4b0b-933f-1006c704fd6c";
    private final String SENDING_URL        = "https://firebasestorage.googleapis.com/v0/b/etalk-180808.appspot.com/o/status%2Fsending.png?alt=media&token=6ecff111-5197-4a46-940f-7dcb25702f35";
    private final String FAILED_URL         = "https://firebasestorage.googleapis.com/v0/b/etalk-180808.appspot.com/o/status%2Ffailed.png?alt=media&token=d0826d53-2a92-419f-9d30-efce313de1a6";
    private final String FIRST_LOAD         = "first_load";
    private final String LOAD_MORE          = "load_more";
    private final String SEND_TEXT          = "send_text";
    private final String SEND_IMAGE_URI     = "send_image_uri";
    private final String SEND_VIDEO_URI     = "send_video_uri";
    private final String SEND_FILE          = "send_file";
    private final String SEND_LOCATION      = "send_location";
    private final String SEND_AUDIO         = "send_audio";
    private final String START_DOWNLOAD     = "start_download";
    private final String STOP_DOWNLOAD      = "stop_download";
    private final String START_PLAY         = "start_play";
    private final String STOP_PLAY          = "stop_play";

    private Context                 context;
    private Executor                executor;
    private Scheduler               scheduler;
    private CompositeDisposable     disposable;
    private UserRepository          userRepository;
    private ConversationRepository  conversationRepository;

    private Handler messageHandler      = new Handler();
    private boolean needUpdateMessage   = false;

    @Inject
    public ChatGroupUsecase(Context context,
                            Executor executor,
                            Scheduler scheduler,
                            CompositeDisposable disposable,
                            UserRepository userRepository,
                            ConversationRepository conversationRepository) {
        this.context                = context;
        this.executor               = executor;
        this.scheduler              = scheduler;
        this.disposable             = disposable;
        this.userRepository         = userRepository;
        this.conversationRepository = conversationRepository;
    }

    private String username;
    private String conversationKey;

    private Conversation oldConversation, newConversation;
    private Map<String, User>   friends = new HashMap<>();

    private MessagesGroupHolder holder = new MessagesGroupHolder();

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void execute(Object observer, Object... params) {
        switch ((String)params[2]) {
            case SEND_TEXT:
                executeSendMessage((String)params[0]);
                break;
            case SEND_IMAGE_URI:
                executeSendImageUri((Uri)params[0]);
                break;
            case SEND_FILE:
                executeSendFile((Uri)params[0]);
                break;
            case FIRST_LOAD:
                username        = (String)params[0];
                conversationKey = (String)params[1];
                executeFirstLoadCase((DisposableObserver<Event>)observer);
                break;
            case LOAD_MORE:
                break;
            case START_DOWNLOAD:
                executeStartDownload((int)params[0], (ViewModelCallback)params[1]);
                break;
            case STOP_DOWNLOAD:
                executeStopDownload();
                break;
            case SEND_LOCATION:
                executeSendLocation((double)params[0], (double)params[1]);
                break;
            case SEND_AUDIO:
                executeSendAudio((Uri)params[0], (long)params[1]);
                break;
            case START_PLAY:
                executeStartPlay((int)params[0], (ViewModelCallback)params[1]);
                break;
            case STOP_PLAY:
                executeStopPlay();
                break;
            case SEND_VIDEO_URI:
                executeSendVideoUri((Uri)params[0]);
        }
    }

    @SuppressLint("CheckResult")
    private void executeFirstLoadCase(DisposableObserver<Event> observer) {
        disposable.add(
                buildFirstLoadObservable()
                        .subscribeOn(Schedulers.from(executor))
                        .observeOn(scheduler)
                        .subscribeWith(observer)
        );
    }

    private Observable<Event> buildFirstLoadObservable() {
        return Observable.create(emitter -> {
            runMessageHandler(emitter);
            loadConversation();
        });
    }

    private void runMessageHandler(ObservableEmitter<Event> emitter) {
        messageHandler.postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        if (needUpdateMessage) {
                            needUpdateMessage = false;
                            emitter.onNext(Event.create(
                                    Event.CHAT_ACTIVITY_MESSAGES,
                                    holder.getMessages()
                            ));
                        }
                        messageHandler.postDelayed(this, 500);
                    }
                },
                500
        );
    }

    @SuppressLint("CheckResult")
    private void loadConversation() {
        disposable.add(
                conversationRepository
                        .loadLocalConversation(conversationKey)
                        .subscribeOn(Schedulers.from(executor))
                        .observeOn(scheduler)
                        .subscribeWith(new DisposableSingleObserver<Conversation>() {
                            @Override
                            public void onSuccess(Conversation item) {
                                newConversation = item;
                                loadFriend();
                                onError(null);
                            }

                            @Override
                            public void onError(Throwable e) {
                                disposable.add(
                                        conversationRepository
                                                .loadNetworkConversation(conversationKey)
                                                .subscribe(item -> {
                                                    oldConversation = newConversation;
                                                    newConversation = item;
                                                    if (oldConversation == null) {
                                                        loadFriend();
                                                    }
                                                    if (friends.size() == newConversation.getMembers().size() - 1) {
                                                        for (Map.Entry<String, Long> entry : newConversation.getMembers().entrySet()) {
                                                            if (!entry.getKey().equals(username)) {
                                                                if (entry.getValue() != oldConversation.getMembers().get(entry.getKey())) {
                                                                    new Handler().postDelayed(
                                                                            () -> {
                                                                                holder.newConversationInfo(oldConversation, newConversation);
                                                                                needUpdateMessage = true;
                                                                            },
                                                                            500
                                                                    );
                                                                    break;
                                                                }
                                                            }
                                                        }
                                                    }
                                                    conversationRepository.insertLocalConversation(newConversation);
                                                })
                                );
                            }
                        })
        );
    }

    private void loadFriend() {
        for (String key : newConversation.getMembers().keySet()) {
            if (!key.equals(username)) {
                disposable.add(
                        userRepository
                                .loadLocalUser(key)
                                .subscribeOn(Schedulers.from(executor))
                                .observeOn(scheduler)
                                .subscribeWith(new DisposableSingleObserver<User>() {
                                    @RequiresApi(api = Build.VERSION_CODES.N)
                                    @Override
                                    public void onSuccess(User user) {
                                        friends.put(user.getUsername(), user);
                                        if (friends.size() == newConversation.getMembers().size() - 1) {
                                            loadMessages();
                                        }
                                        onError(null);
                                    }

                                    @RequiresApi(api = Build.VERSION_CODES.N)
                                    @Override
                                    public void onError(Throwable e) {
                                        disposable.add(
                                                userRepository
                                                        .loadNetworlChangeableUser(key)
                                                        .subscribe(userOptional -> {
                                                            if (friends.containsKey(userOptional.get().getUsername())) {
                                                                friends.replace(
                                                                        userOptional.get().getUsername(),
                                                                        userOptional.get()
                                                                );
                                                            } else {
                                                                friends.put(
                                                                        userOptional.get().getUsername(),
                                                                        userOptional.get()
                                                                );
                                                                if (friends.size() == newConversation.getMembers().size() - 1) {
                                                                    loadMessages();
                                                                }
                                                            }
                                                        })
                                        );
                                    }
                                })
                );
            }
        }
    }

    private void loadMessages() {
        disposable.add(
                conversationRepository
                        .loadLocalMessagesGroupHolder(this, conversationKey)
                        .subscribeOn(Schedulers.from(executor))
                        .observeOn(scheduler)
                        .subscribeWith(new DisposableSingleObserver<MessagesGroupHolder>(){
                            @RequiresApi(api = Build.VERSION_CODES.N)
                            @Override
                            public void onSuccess(MessagesGroupHolder messagesGroupHolder) {
                                holder = messagesGroupHolder;
                                needUpdateMessage = true;
                                resendFailedMessage(holder.getSendingMessage().values());
                                onError(null);
                            }
                            @Override
                            public void onError(Throwable e) {
                                disposable.add(
                                        conversationRepository
                                                .loadNetworkMessages(conversationKey, username)
                                                .subscribe(message -> {
                                                    holder.addNewMessage(message);
                                                    needUpdateMessage = true;
                                                })
                                );
                            }
                        })
        );
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void resendFailedMessage(Collection<Message> collection) {
        for (Message message : collection) {
            if (message.getType() == Message.TEXT) {
                disposable.add(
                        conversationRepository
                                .pushNetworkMessage(conversationKey, message)
                                .subscribeOn(Schedulers.from(executor))
                                .observeOn(scheduler)
                                .subscribe(result -> {
                                    if (result == true) {
                                        holder.sendSuccessMessage(message);
                                        needUpdateMessage = true;
                                    }
                                })
                );
            } else if (message.getType() == Message.IMAGE) {
                String[] dataPart = message.getData().split("eTaLkImAgE");
                if (dataPart[0].contains("firebasestorage")) {
                    disposable.add(
                            conversationRepository
                                    .pushNetworkMessage(conversationKey, message)
                                    .subscribeOn(Schedulers.from(executor))
                                    .observeOn(scheduler)
                                    .subscribe(result -> {
                                        if (result == true) {
                                            holder.sendSuccessMessage(message);
                                            needUpdateMessage = true;
                                        }
                                    })
                    );
                } else {
                    File internalFile = new File(context.getFilesDir(), "IMG_" + message.getKey() + ".png");
                    if (internalFile.exists()) {
                        disposable.add(
                                conversationRepository
                                        .uploadNetworkFile(conversationKey, internalFile, Message.IMAGE)
                                        .subscribeOn(Schedulers.from(executor))
                                        .observeOn(scheduler)
                                        .subscribe(url -> {
                                            internalFile.delete();
                                            message.setData(url + "eTaLkImAgE" + dataPart[1] + "eTaLkImAgE" + dataPart[2]);
                                            disposable.add(
                                                    conversationRepository
                                                            .pushNetworkMessage(conversationKey, message)
                                                            .subscribeOn(Schedulers.from(executor))
                                                            .observeOn(scheduler)
                                                            .subscribe(result -> {
                                                                if (result == true) {
                                                                    holder.sendSuccessMessage(message);
                                                                    needUpdateMessage = true;
                                                                }
                                                            })
                                            );
                                        })
                        );
                    } else {
                        File externalFile = new File(dataPart[0]);
                        if (externalFile.exists()) {
                            Bitmap bitmap = Tool.getImageWithUri(context, Uri.fromFile(externalFile));
                            disposable.add(
                                    resizeBitmapToInternalStorage(bitmap, message.getKey())
                                            .subscribeOn(Schedulers.from(executor))
                                            .observeOn(scheduler)
                                            .subscribe(file -> {
                                                disposable.add(
                                                        conversationRepository
                                                                .uploadNetworkFile(conversationKey, file, Message.IMAGE)
                                                                .subscribeOn(Schedulers.from(executor))
                                                                .observeOn(scheduler)
                                                                .subscribe(url -> {
                                                                    file.delete();
                                                                    message.setData(url + "eTaLkImAgE" + bitmap.getWidth() + "eTaLkImAgE" + bitmap.getHeight());
                                                                    disposable.add(
                                                                            conversationRepository
                                                                                    .pushNetworkMessage(conversationKey, message)
                                                                                    .subscribeOn(Schedulers.from(executor))
                                                                                    .observeOn(scheduler)
                                                                                    .subscribe(result -> {
                                                                                        if (result == true) {
                                                                                            holder.sendSuccessMessage(message);
                                                                                            needUpdateMessage = true;
                                                                                        }
                                                                                    })
                                                                    );
                                                                })
                                                );
                                            })
                            );
                        } else {
                            holder.sendFailedMessage(message);
                            needUpdateMessage = true;
                        }
                    }
                }
            } else if (message.getType() == Message.FILE) {
                String[] dataPart = message.getData().split("eTaLkFiLe");
                if (dataPart[0].contains("firebasestorage")) {
                    disposable.add(
                            conversationRepository
                                    .pushNetworkMessage(conversationKey, message)
                                    .subscribeOn(Schedulers.from(executor))
                                    .observeOn(scheduler)
                                    .subscribe(result -> {
                                        if (result == true) {
                                            holder.sendSuccessMessage(message);
                                            needUpdateMessage = true;
                                        }
                                    })
                    );
                } else {
                    String[] fileNamePart = dataPart[1].split("\\.");
                    File file = new File(
                            context.getFilesDir(),
                            fileNamePart[0] + "_" + message.getKey() + "." + fileNamePart[1]
                    );
                    if (file.exists()) {
                        disposable.add(
                                conversationRepository
                                        .uploadNetworkFile(conversationKey, file, Message.FILE)
                                        .subscribeOn(Schedulers.from(executor))
                                        .observeOn(scheduler)
                                        .subscribe(url -> {
                                            file.delete();
                                            message.setData(url + "eTaLkFiLe" + dataPart[1]);
                                            disposable.add(
                                                    conversationRepository
                                                            .pushNetworkMessage(conversationKey, message)
                                                            .subscribeOn(Schedulers.from(executor))
                                                            .observeOn(scheduler)
                                                            .subscribe(result -> {
                                                                if (result == true) {
                                                                    holder.sendSuccessMessage(message);
                                                                    needUpdateMessage = true;
                                                                }
                                                            })
                                            );
                                        })
                        );
                    } else {
                        message.setData("failed" + "eTaLkFiLe" + dataPart[1]);
                        holder.sendFailedMessage(message);
                        needUpdateMessage = true;
                    }
                }
            } else if (message.getType() == Message.SOUND) {
                if (!message.getData().isEmpty()) {
                    disposable.add(
                            conversationRepository
                                    .pushNetworkMessage(conversationKey, message)
                                    .subscribeOn(Schedulers.from(executor))
                                    .observeOn(scheduler)
                                    .subscribe(result -> {
                                        if (result == true) {
                                            holder.sendSuccessMessage(message);
                                            needUpdateMessage = true;
                                        }
                                    })
                    );
                } else {
                    message.setData("failed");
                    holder.sendFailedMessage(message);
                }
            } else if (message.getType() == Message.VIDEO) {
                String[] dataPart = message.getData().split("eTaLkViDeO");
                if (dataPart.length == 4) {
                    disposable.add(
                            conversationRepository
                                    .pushNetworkMessage(conversationKey, message)
                                    .subscribeOn(Schedulers.from(executor))
                                    .observeOn(scheduler)
                                    .subscribe(result -> {
                                        if (result == true) {
                                            holder.sendSuccessMessage(message);
                                            needUpdateMessage = true;
                                        }
                                    })
                    );
                } else {
                    File thumbnailFile = new File(
                            context.getFilesDir(),
                            "VID_" + message.getKey() + "_thumbnail.png"
                    );
                    File videoFile = new File(
                            context.getFilesDir(),
                            "VID_" + message.getKey() + ".mp4"
                    );
                    if (thumbnailFile.exists() && videoFile.exists()) {
                        disposable.add(
                                conversationRepository
                                        .uploadNetworkFile(conversationKey, thumbnailFile, Message.VIDEO)
                                        .zipWith(
                                                conversationRepository.uploadNetworkFile(conversationKey, videoFile, Message.VIDEO),
                                                ((thumbnailUrl, videoUrl) -> new String[]{thumbnailUrl, videoUrl})
                                        )
                                        .subscribeOn(Schedulers.from(executor))
                                        .observeOn(scheduler)
                                        .subscribe(urls -> {
                                            thumbnailFile.delete();
                                            videoFile.delete();
                                            message.setData(urls[0] + "eTaLkViDeO" + urls[1] + "eTaLkViDeO" + message.getData());
                                            disposable.add(
                                                    conversationRepository
                                                            .pushNetworkMessage(conversationKey, message)
                                                            .subscribeOn(Schedulers.from(executor))
                                                            .observeOn(scheduler)
                                                            .subscribe(result -> {
                                                                if (result == true) {
                                                                    holder.sendSuccessMessage(message);
                                                                    needUpdateMessage = true;
                                                                }
                                                            })
                                            );
                                        })
                        );
                    } else {
                        if (thumbnailFile.exists()) thumbnailFile.delete();
                        if (videoFile.exists()) videoFile.delete();
                        message.setData("failedeTaLkViDeO" + message.getData());
                        holder.sendFailedMessage(message);
                    }
                }
            }
        }
    }

    private void executeSendMessage(String text) {
        Message message = new Message(
                username,
                formatText(text),
                Message.TEXT
        );
        holder.sendNewMessage(message);
        needUpdateMessage = true;
        disposable.add(
                conversationRepository
                        .pushNetworkMessage(conversationKey, message)
                        .subscribeOn(Schedulers.from(executor))
                        .observeOn(scheduler)
                        .subscribe(result -> {
                            if (result == true) {
                                holder.sendSuccessMessage(message);
                                needUpdateMessage = true;
                            }
                        })
        );
    }

    private String formatText(String text) {
        int firstMark, secondMark;
        while ((firstMark = text.indexOf("*")) >= 0) {
            if ((secondMark = text.indexOf("*", firstMark + 1)) < 0) break;
            text = text
                    .replaceFirst("\\*", "<b>")
                    .replaceFirst("\\*", "</b>");
        }
        while ((firstMark = text.indexOf("~")) >= 0) {
            if ((secondMark = text.indexOf("~", firstMark + 1)) < 0) break;
            text = text
                    .replaceFirst("~", "<i>")
                    .replaceFirst("~", "</i>");
        }
        while ((firstMark = text.indexOf("_")) >= 0) {
            if ((secondMark = text.indexOf("_", firstMark + 1)) < 0) break;
            text = text
                    .replaceFirst("_", "<u>")
                    .replaceFirst("_", "</u>");
        }
        return text;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void executeSendImageUri(Uri uri) {
        Bitmap bitmap = Tool.getImageWithUri(context, uri);
        Message message = new Message(
                username,
                 uri.toString() + "eTaLkImAgE" + bitmap.getWidth() + "eTaLkImAgE" + bitmap.getHeight(),
                Message.IMAGE
        );
        holder.sendNewMessage(message);
        needUpdateMessage = true;
        disposable.add(
                resizeBitmapToInternalStorage(bitmap, message.getKey())
                        .subscribeOn(Schedulers.from(executor))
                        .observeOn(scheduler)
                        .subscribe(file -> {
                            disposable.add(
                                    conversationRepository
                                            .uploadNetworkFile(conversationKey, file, Message.IMAGE)
                                            .subscribeOn(Schedulers.from(executor))
                                            .observeOn(scheduler)
                                            .subscribe(url -> {
                                                file.delete();
                                                message.setData(url + "eTaLkImAgE" + bitmap.getWidth() + "eTaLkImAgE" + bitmap.getHeight());
                                                disposable.add(
                                                        conversationRepository
                                                                .pushNetworkMessage(conversationKey, message)
                                                                .subscribeOn(Schedulers.from(executor))
                                                                .observeOn(scheduler)
                                                                .subscribe(result -> {
                                                                    if (result == true) {
                                                                        holder.sendSuccessMessage(message);
                                                                        needUpdateMessage = true;
                                                                    }
                                                                })
                                                );
                                            })
                            );
                        })
        );
    }

    private Single<File> resizeBitmapToInternalStorage(Bitmap bitmap, String key) {
        return Single.create(emitter -> {
            Bitmap bm = bitmap;
            if (bm.getWidth() > 512 || bm.getHeight() > 512) {
                bm = Tool.resizeImage(bitmap, 512);
            }
            File file = new File(context.getFilesDir(), "IMG_" + key + ".png");
            try {
                OutputStream os = new FileOutputStream(file);
                bm.compress(Bitmap.CompressFormat.PNG, 0, os);
                os.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            emitter.onSuccess(file);
        });
    }

    private void executeSendVideoUri(Uri uri) {

        Bitmap bitmap = makeThumbnail(context, uri);

        Message message = new Message(
                username,
                bitmap.getWidth() + "eTaLkViDeO" + bitmap.getHeight(),
                Message.VIDEO
        );

        holder.sendNewMessage(message);
        needUpdateMessage = true;

        File videoFile = new File(
                context.getFilesDir(),
                "VID_" + message.getKey() + ".mp4"
        );

        VideoCompress.compressVideoLow(getRealPathFromURI(context, uri), videoFile.getAbsolutePath(), new VideoCompress.CompressListener() {
            @Override
            public void onStart() {}

            @Override
            public void onSuccess() {
                disposable.add(
                        makeThumbnailFileInInternalStorage(Uri.fromFile(videoFile), message.getKey())
                                .subscribeOn(Schedulers.from(executor))
                                .observeOn(scheduler)
                                .subscribe(thumbnailFile ->
                                        disposable.add(
                                                conversationRepository
                                                        .uploadNetworkFile(conversationKey, thumbnailFile, Message.VIDEO)
                                                        .zipWith(
                                                                conversationRepository.uploadNetworkFile(conversationKey, videoFile, Message.VIDEO),
                                                                ((thumbnailUrl, videoUrl) -> new String[]{thumbnailUrl, videoUrl})
                                                        )
                                                        .subscribeOn(Schedulers.from(executor))
                                                        .observeOn(scheduler)
                                                        .subscribe(urls -> {
                                                            thumbnailFile.delete();
                                                            videoFile.delete();
                                                            message.setData(urls[0] + "eTaLkViDeO" + urls[1] + "eTaLkViDeO" + message.getData());
                                                            disposable.add(
                                                                    conversationRepository
                                                                            .pushNetworkMessage(conversationKey, message)
                                                                            .subscribeOn(Schedulers.from(executor))
                                                                            .observeOn(scheduler)
                                                                            .subscribe(result -> {
                                                                                if (result == true) {
                                                                                    holder.sendSuccessMessage(message);
                                                                                    needUpdateMessage = true;
                                                                                }
                                                                            })
                                                            );
                                                        })
                                        )
                                )
                );
            }

            @Override
            public void onFail() {
                Log.i("eTalk", "Compress video failed");
            }

            @Override
            public void onProgress(float percent) {}
        });
    }

    private String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } catch (Exception e) {
            Log.e("eTalk", e.getMessage());
            return null;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private Bitmap makeThumbnail(Context context, Uri uri) {
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(context, uri);
        return mediaMetadataRetriever.getFrameAtTime();
    }

    private Single<File> makeThumbnailFileInInternalStorage(Uri uri, String key) {
        return Single.create(emitter -> {
            MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
            mediaMetadataRetriever.setDataSource(context, uri);
            Bitmap bitmap = mediaMetadataRetriever.getFrameAtTime();
            File file = new File(context.getFilesDir(), "VID_" + key + "_thumbnail.png");
            try {
                OutputStream os = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 0, os);
                os.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            emitter.onSuccess(file);
        });
    }

    private void executeSendFile(Uri uri) {
        String fileName = getFileName(uri);
        Message message = new Message(
                username,
                "null" + "eTaLkFiLe" + fileName,
                Message.FILE
        );
        holder.sendNewMessage(message);
        needUpdateMessage = true;
        String[] fileNamePart = fileName.split("\\.");
        disposable.add(
                copyToInternalStorageFromUri(
                        uri,
                        fileNamePart[0] + "_" + message.getKey() + "." + fileNamePart[1],
                        Message.FILE
                )
                        .subscribeOn(Schedulers.from(executor))
                        .observeOn(scheduler)
                        .subscribe(file ->
                                disposable.add(
                                        conversationRepository
                                                .uploadNetworkFile(conversationKey, file, Message.FILE)
                                                .subscribeOn(Schedulers.from(executor))
                                                .observeOn(scheduler)
                                                .subscribe(url -> {
                                                    file.delete();
                                                    message.setData(url + "eTaLkFiLe" + fileName);
                                                    disposable.add(
                                                            conversationRepository
                                                                    .pushNetworkMessage(conversationKey, message)
                                                                    .subscribeOn(Schedulers.from(executor))
                                                                    .observeOn(scheduler)
                                                                    .subscribe(result -> {
                                                                        if (result == true) {
                                                                            holder.sendSuccessMessage(message);
                                                                            needUpdateMessage = true;
                                                                        }
                                                                    })
                                                    );
                                                })
                                )
                        )
        );
    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    private void executeSendAudio(Uri uri, long time) {
        Message message = new Message(
                username,
                "",
                Message.SOUND
        );
        holder.sendNewMessage(message);
        needUpdateMessage = true;
        disposable.add(
                copyToInternalStorageFromUri(
                        uri,
                        "AUD_" + message.getKey() + ".3gp",
                        Message.SOUND
                )
                        .subscribeOn(Schedulers.from(executor))
                        .observeOn(scheduler)
                        .subscribe(file ->
                                disposable.add(
                                        conversationRepository
                                                .uploadNetworkFile(conversationKey, file, Message.SOUND)
                                                .subscribeOn(Schedulers.from(executor))
                                                .observeOn(scheduler)
                                                .subscribe(url -> {
                                                    file.delete();
                                                    message.setData(url + "eTaLkAuDiO" + time);
                                                    disposable.add(
                                                            conversationRepository
                                                                    .pushNetworkMessage(conversationKey, message)
                                                                    .subscribeOn(Schedulers.from(executor))
                                                                    .observeOn(scheduler)
                                                                    .subscribe(result -> {
                                                                        if (result == true) {
                                                                            holder.sendSuccessMessage(message);
                                                                            needUpdateMessage = true;
                                                                        }
                                                                    })
                                                    );
                                                })
                            )
                        )
        );
    }

    private Single<File> copyToInternalStorageFromUri(Uri uri, String name, long type) {
         return Single.create(emitter -> {
             File file = null;
             if (type == Message.FILE) {
                 file = new File(
                         context.getFilesDir(),
                         name
                 );
             } else if (type == Message.SOUND) {
                 file = new File(
                         context.getFilesDir(),
                         name
                 );
             }
             try {
                 InputStream is = context.getContentResolver().openInputStream(uri);
                 OutputStream os = new FileOutputStream(file);
                 byte[] buff = new byte[1024];
                 int len;
                 while((len = is.read(buff)) > 0){
                     os.write(buff,0, len);
                 }
                 is.close();
                 os.close();
             } catch (FileNotFoundException e) {
                 e.printStackTrace();
             } catch (IOException e) {
                 e.printStackTrace();
             }
             emitter.onSuccess(file);
        });
    }

    private Disposable downloadDisposable = null;
    private int downloadingIndex = -1;

    @SuppressLint("CheckResult")
    private void executeStartDownload(int index, ViewModelCallback callback) {
        if (index != -1) {
            if (downloadingIndex == -1) {
                downloadingIndex = index;
                String url = holder.startDownloadAt(index);
                if (url != null) {
                    needUpdateMessage = true;
                    conversationRepository
                            .downloadNetworkFile(url)
                            .subscribeOn(Schedulers.from(executor))
                            .observeOn(scheduler)
                            .subscribeWith(new Observer<Event>() {
                                @Override
                                public void onSubscribe(Disposable d) {
                                    downloadDisposable = d;
                                }

                                @Override
                                public void onNext(Event event) {
                                    switch (event.getType()) {
                                        case Event.CHAT_ACTIVITY_DOWNLOAD_OK:
                                            holder.stopDownloadAt(index);
                                            needUpdateMessage = true;
                                            callback.onHelp(Event.create(
                                                    Event.CHAT_ACTIVITY_DOWNLOAD_OK,
                                                    "\\download\\eTalk\\" + url.split("eTaLkFiLe")[1]
                                            ));
                                            downloadingIndex = -1;
                                            break;
                                        case Event.CHAT_ACTIVITY_DOWNLOAD_FAILED:
                                            holder.stopDownloadAt(index);
                                            needUpdateMessage = true;
                                            callback.onHelp(Event.create(Event.CHAT_ACTIVITY_DOWNLOAD_FAILED));
                                            downloadingIndex = -1;
                                            break;
                                        case Event.CHAT_ACTIVITY_DOWNLOAD_PROGRESS:
                                            holder.updateProgressAt(index, ((Long) event.getData()[0]).intValue());
                                            needUpdateMessage = true;
                                            break;
                                    }
                                }

                                @Override
                                public void onError(Throwable e) {

                                }

                                @Override
                                public void onComplete() {

                                }
                            });
                }
            }
        }
    }

    private void executeStopDownload() {
        if (downloadingIndex != -1) {
            holder.stopDownloadAt(downloadingIndex);
            downloadingIndex = -1;
            downloadDisposable.dispose();
            needUpdateMessage = true;
        }
    }

    private ExoPlayer player;
    private Handler playHandler = new Handler();
    private int playIndex = -1;

    private void executeStartPlay(int index, ViewModelCallback callback) {
        if (index != -1) {
            if (playIndex == -1) {
                playIndex = index;
                String url = holder.startPlayAt(index);
                if (url != null) {
                    needUpdateMessage = true;
                    player = ExoPlayerFactory.newSimpleInstance(
                            new DefaultRenderersFactory(context),
                            new DefaultTrackSelector(
                                    new AdaptiveTrackSelection.Factory(
                                            new DefaultBandwidthMeter()
                                    )
                            ),
                            new DefaultLoadControl()
                    );
                    player.setPlayWhenReady(true);
                    player.addListener(new Player.DefaultEventListener() {
                        @Override
                        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                            switch (playbackState) {
                                case PlaybackStateCompat.STATE_PLAYING:
                                    playHandler.postDelayed(
                                            new Runnable() {
                                                @Override
                                                public void run() {
                                                    playHandler.postDelayed(this, 500);
                                                    holder.updateTimeAt(playIndex, (int) (player.getDuration() - player.getCurrentPosition()) / 1000);
                                                    needUpdateMessage = true;
                                                }
                                            },
                                            500
                                    );
                                    break;
                                case PlaybackStateCompat.STATE_FAST_FORWARDING:
                                    playHandler.removeCallbacksAndMessages(null);
                                    executeStopPlay();
                                    break;
                            }
                        }
                    });
                    player.prepare(
                            new ExtractorMediaSource.Factory(
                                    new DefaultHttpDataSourceFactory(context.getPackageName())
                            ).createMediaSource(
                                    Uri.parse(url.split("eTaLkAuDiO")[0])
                            )
                    );
                }
            } else {
                executeStopPlay();
                executeStartPlay(index, callback);
            }
        }
    }

    private void executeStopPlay() {
        if (playIndex != -1) {
            holder.stopPlayAt(playIndex);
            playHandler.removeCallbacksAndMessages(null);
            playIndex = -1;
            player.stop();
            player.release();
            player = null;
            needUpdateMessage = true;
        }
    }

    private void executeSendLocation(double lat, double lng) {
        Message message = new Message(
                username,
                lat + ", " + lng,
                Message.MAP
        );
        holder.sendNewMessage(message);
        needUpdateMessage = true;
        disposable.add(
                conversationRepository
                        .pushNetworkMessage(conversationKey, message)
                        .subscribeOn(Schedulers.from(executor))
                        .observeOn(scheduler)
                        .subscribe(result -> {
                            if (result == true) {
                                holder.sendSuccessMessage(message);
                                needUpdateMessage = true;
                            }
                        })
        );
    }

    @Override
    public void endTask() {
        messageHandler.removeCallbacksAndMessages(null);
        executeStopDownload();
        executeStopPlay();
        conversationRepository.putLocalMessagesGroupHolder(
                conversationKey,
                holder
        );
        if (disposable.size() > 0) {
            disposable.clear();
        }
    }

    public class MessagesGroupHolder{

        Map<String, Message>    rawMessages     = new HashMap<>();
        Map<String, Message>    sendingMessage  = new HashMap<>();
        List<MessageGroupItem>  messages        = new ArrayList<>();

        public MessagesGroupHolder() {}

        public MessagesGroupHolder(Map<String, Message> rawMessages,
                                   Map<String, Message> sendingMessage,
                                   List<MessageGroupItem> messages) {
            this.rawMessages    = rawMessages;
            this.sendingMessage = sendingMessage;
            this.messages       = messages;
        }

        public void addNewMessage(Message message) {
            if (!rawMessages.containsKey(message.getKey())) {
                rawMessages.put(
                        message.getKey(),
                        message
                );
                MessageGroupItem newItem = new MessageGroupItem(message)
                        .newData(message.getData())
                        .newTime(decodeTime(message.getTime()))
                        .newTimeVisible(View.VISIBLE);
                if (message.getSender().equals(username)) {
                    newItem = newItem
                            .newMe(true)
                            .newAvatar(SENT_URL)
                            .newAvatarVisible(View.VISIBLE);
                    Map<String, String> seens = newItem.getSeen();
                    for (Map.Entry<String, Long> entry : newConversation.getMembers().entrySet()) {
                        if (!entry.getKey().equals(username)) {
                            if (entry.getValue() > message.getTime()) {
                                seens.put(
                                        entry.getKey(),
                                        friends.get(entry.getKey()).getAvatar()
                                );
                                for (int i = messages.size() - 1; i >= 0; --i) {
                                    if (messages.get(i).getSeen().containsKey(entry.getKey())){
                                        Map<String, String> map = messages.get(i).getSeen();
                                        map.remove(entry.getKey());
                                        messages.set(
                                                i,
                                                messages.get(i).newSeen(map)
                                        );
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    if (!seens.isEmpty()) {
                        newItem = newItem
                                .newSeen(seens)
                                .newAvatarVisible(View.GONE);
                        for (int i = messages.size() - 1; i >= 0; --i) {
                            if (messages.get(i).isMe() && !sendingMessage.containsKey(messages.get(i).getMessage().getKey())) {
                                if (messages.get(i).getAvatarVisible() == View.GONE) break;
                                messages.set(
                                        i,
                                        messages.get(i).newAvatarVisible(View.GONE)
                                );
                            }
                        }
                    }
                    if (!messages.isEmpty() && messages.get(messages.size() - 1).isMe()) {
                        messages.set(
                                messages.size() - 1,
                                messages.get(messages.size() - 1).newTimeVisible(View.GONE)
                        );
                    }
                } else {
                    newItem = newItem
                            .newMe(false)
                            .newName(friends.get(message.getSender()).getName())
                            .newNameVisible(View.VISIBLE)
                            .newAvatar(friends.get(message.getSender()).getAvatar())
                            .newAvatarVisible(View.VISIBLE);
                    Map<String, String> seens = newItem.getSeen();
                    for (Map.Entry<String, Long> entry : newConversation.getMembers().entrySet()) {
                        if (!entry.getKey().equals(username) && !entry.getKey().equals(message.getSender())) {
                            if (entry.getValue() > message.getTime()) {
                                seens.put(
                                        entry.getKey(),
                                        friends.get(entry.getKey()).getAvatar()
                                );
                                for (int i = messages.size() - 1; i >= 0; --i) {
                                    if (messages.get(i).getSeen().containsKey(entry.getKey())){
                                        Map<String, String> map = messages.get(i).getSeen();
                                        map.remove(entry.getKey());
                                        messages.set(
                                                i,
                                                messages.get(i).newSeen(map)
                                        );
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    if (!seens.isEmpty()) {
                        newItem = newItem.newSeen(seens);
                        for (int i = messages.size() - 1; i >= 0; --i) {
                            if (messages.get(i).isMe() && !sendingMessage.containsKey(messages.get(i).getMessage().getKey())) {
                                if (messages.get(i).getAvatarVisible() == View.GONE) break;
                                messages.set(
                                        i,
                                        messages.get(i).newAvatarVisible(View.GONE)
                                );
                            }
                        }
                    }
                    if (!messages.isEmpty() && messages.get(messages.size() - 1).getMessage().getSender().equals(newItem.getMessage().getSender())) {
                        messages.set(
                                messages.size() - 1,
                                messages.get(messages.size() - 1).newTimeVisible(View.GONE)
                        );
                        newItem = newItem
                                .newAvatarVisible(View.GONE)
                                .newNameVisible(View.GONE);
                    }
                    if (newConversation.getType() == Conversation.PERSON && newItem.getNameVisible() == View.VISIBLE) {
                        newItem = newItem.newNameVisible(View.GONE);
                    }
                }
                messages.add(newItem);
            } else if (sendingMessage.containsKey(message.getKey())) {
                sendingMessage.get(message.getKey()).setServerTime(message.getTime());
            }
        }

        public void sendNewMessage(Message message) {
            sendingMessage.put(message.getKey(), message);
            rawMessages.put(message.getKey(), message);
            MessageGroupItem newItem = new MessageGroupItem(
                    message,
                    true,
                    null,
                    View.GONE,
                    message.getData(),
                    SENDING_URL,
                    View.VISIBLE,
                    decodeTime(Long.parseLong(message.getKey().substring(username.length()))),
                    View.VISIBLE
            );
            messages.add(newItem);
            if (messages.size() > 1 && messages.get(messages.size() - 2).isMe()) {
                messages.set(
                        messages.size() - 2,
                        messages.get(messages.size() - 2).newTimeVisible(View.GONE)
                );
            }
        }

        public void sendSuccessMessage(Message message) {
            sendingMessage.remove(message.getKey());
            for (int i = messages.size() - 1; i >= 0; --i) {
                if (messages.get(i).getMessage().getKey().equals(message.getKey())) {
                    messages.set(
                            i,
                            messages.get(i).newAvatar(SENT_URL)
                    );
                    messages.get(i).getMessage().setServerTime(Long.parseLong(message.getKey().substring(username.length())));
                    if (message.getType() != Message.TEXT) {
                        messages.set(
                                i,
                                messages.get(i).newMessage(message).newData(message.getData())
                        );
                    }
                    break;
                }
            }
        }

        public void sendFailedMessage(Message message) {
            sendingMessage.remove(message.getKey());
            for (int i = messages.size() - 1; i >= 0; --i) {
                if (messages.get(i).getMessage().getKey().equals(message.getKey())) {
                    messages.set(
                            i,
                            messages.get(i).newAvatar(FAILED_URL).newData(message.getData())
                    );
                    messages
                            .get(i)
                            .getMessage()
                            .setServerTime(Long.parseLong(message.getKey().substring(username.length())));
                    if (message.getType() == Message.FILE || message.getType() == Message.VIDEO) {
                        messages.set(
                                i,
                                messages.get(i).newStartVisible(View.GONE)
                        );
                    }
                    break;
                }
            }
        }

        public void newConversationInfo(Conversation oldConversation, Conversation newConversation) {
            for (Map.Entry<String, Long> entry : newConversation.getMembers().entrySet()) {
                if (!entry.getKey().equals(username)) {
                    if (entry.getValue() != oldConversation.getMembers().get(entry.getKey())) {
                        for (int i = messages.size() - 1; i >= 0; --i) {
                            if (!sendingMessage.containsKey(messages.get(i).getMessage().getKey())
                                    && !messages.get(i).getAvatar().equalsIgnoreCase(FAILED_URL)) {
                                if (entry.getValue() > messages.get(i).getMessage().getTime()
                                        && !entry.getKey().equals(messages.get(i).getMessage().getSender())) {
                                    if (!messages.get(i).getSeen().containsKey(entry.getKey())) {
                                        Map<String, String> map = messages.get(i).getSeen();
                                        map.put(
                                                entry.getKey(),
                                                friends.get(entry.getKey()).getAvatar()
                                        );
                                        messages.set(
                                                i,
                                                messages.get(i).newSeen(map)
                                        );
                                        for (int j = i; j >= 0; --j) {
                                            if (messages.get(j).isMe() && !sendingMessage.containsKey(messages.get(j).getMessage().getKey())){
                                                if (messages.get(j).getAvatarVisible() == View.GONE) break;
                                                messages.set(
                                                        j,
                                                        messages.get(j).newAvatarVisible(View.GONE)
                                                );
                                            }
                                        }
                                        for (int j = i - 1; j >= 0; --j) {
                                            if (messages.get(j).getSeen().containsKey(entry.getKey())) {
                                                Map<String, String> _map = messages.get(j).getSeen();
                                                _map.remove(entry.getKey());
                                                messages.set(
                                                        j,
                                                        messages.get(j).newSeen(_map)
                                                );
                                                break;
                                            }
                                        }
                                    }
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }

        public String startDownloadAt(int index) {
            if (sendingMessage.containsKey(messages.get(index).getMessage().getKey())) {
                return null;
            } else {
                messages.set(
                        index,
                        messages.get(index)
                                .newStartVisible(View.GONE)
                                .newStopVisible(View.VISIBLE)
                                .newProgressVisible(View.VISIBLE)
                                .newProgressPercent(0)
                );
                return messages.get(index).getMessage().getData();
            }
        }

        public void updateProgressAt(int index, int percent) {
            messages.set(
                    index,
                    messages.get(index)
                            .newProgressPercent(percent)
            );
        }

        public void stopDownloadAt(int index) {
            messages.set(
                    index,
                    messages.get(index)
                            .newStartVisible(View.VISIBLE)
                            .newStopVisible(View.GONE)
                            .newProgressVisible(View.GONE)
            );
        }

        public String startPlayAt(int index) {
            if (sendingMessage.containsKey(messages.get(index).getMessage().getKey())) {
                return null;
            } else {
                messages.set(
                        index,
                        messages.get(index)
                                .newStartVisible(View.GONE)
                                .newStopVisible(View.VISIBLE)
                );
                return messages.get(index).getMessage().getData();
            }
        }

        public void updateTimeAt(int index, int time) {
            messages.set(
                    index,
                    messages.get(index)
                            .newProgressPercent(time)
                            .newProgressVisible(View.VISIBLE)
            );
        }

        public void stopPlayAt(int index) {
            messages.set(
                    index,
                    messages.get(index)
                            .newStartVisible(View.VISIBLE)
                            .newStopVisible(View.GONE)
                            .newProgressVisible(View.GONE)
            );
        }

        public Map<String, Message> getRawMessages() {
            return rawMessages;
        }

        public void setRawMessages(Map<String, Message> rawMessages) {
            this.rawMessages = rawMessages;
        }

        public Map<String, Message> getSendingMessage() {
            return sendingMessage;
        }

        public void setSendingMessage(Map<String, Message> sendingMessage) {
            this.sendingMessage = sendingMessage;
        }

        public List<MessageGroupItem> getMessages() {
            return new ArrayList<>(messages);
        }

        public void setMessages(List<MessageGroupItem> messages) {
            this.messages = messages;
        }

        private String decodeTime(long time) {
            Calendar when = Calendar.getInstance();
            when.setTimeInMillis(time);
            int hour = when.get(Calendar.HOUR_OF_DAY);
            int minute = when.get(Calendar.MINUTE);
            String minuteZero = minute < 10 ? "0" : "";
            return hour + ":" + minuteZero + minute;
        }
    }
}
