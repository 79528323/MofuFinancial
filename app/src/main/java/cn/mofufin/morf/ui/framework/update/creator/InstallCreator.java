/*
 * Copyright (C) 2017 Haoge
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.mofufin.morf.ui.framework.update.creator;

import android.app.Activity;
import android.app.Dialog;

import cn.mofufin.morf.ui.framework.update.UpdateBuilder;
import cn.mofufin.morf.ui.framework.update.UpdateConfig;
import cn.mofufin.morf.ui.framework.update.model.Update;
import cn.mofufin.morf.ui.framework.update.strategy.UpdateStrategy;
import cn.mofufin.morf.ui.framework.update.util.ActivityManager;
import cn.mofufin.morf.ui.framework.update.util.Recyclable;
import cn.mofufin.morf.ui.framework.update.util.UpdatePreference;

/**
 * <p>此类为当检查到更新时的通知创建器基类。
 * <p>
 * <p>配置方式：通过{@link UpdateConfig#installDialogCreator(InstallCreator)}或者{@link UpdateBuilder#installDialogCreator(InstallCreator)}
 * <p>
 * <p>默认实现：{@link DefaultNeedInstallCreator}
 * <p>
 * <p>触发逻辑：当检查到有新版本更新时且配置的更新策略{@link UpdateStrategy#isAutoInstall()}设定为false时。此通知创建器将被触发
 * <p>
 * <p>定制说明：<br>
 * 1. 当需要进行后续更新操作时(请求调起安装任务)：调用{@link #sendToInstall(String)}}<br>
 * 2. 当需要取消此次更新操作时：调用{@link #sendUserCancel()}<br>
 * 3. 当需要忽略此版本更新时：调用{@link #sendCheckIgnore(Update)}<br>
 *
 * @author haoge
 */
public abstract class InstallCreator implements Recyclable {

    private UpdateBuilder builder;
    private Update update;

    public void setBuilder(UpdateBuilder builder) {
        this.builder = builder;
    }

    public void setUpdate(Update update) {
        this.update = update;
    }

    public abstract Dialog create(Update update, String path, Activity activity);

    public void sendToInstall(String filename) {
        if (builder.getFileChecker().checkAfterDownload(update, filename)) {
            builder.getInstallStrategy().install(ActivityManager.get().getApplicationContext(), filename, update);
        } else {
            builder.getCheckCB().onCheckError(new RuntimeException(String.format("apk %s checked failed", filename)));
        }
        release();
    }

    public void sendUserCancel() {
        if (builder.getCheckCB() != null) {
            builder.getCheckCB().onUserCancel();
        }

        release();
    }

    public void sendCheckIgnore(Update update) {
        if (builder.getCheckCB() != null) {
            builder.getCheckCB().onCheckIgnore(update);
        }
        UpdatePreference.saveIgnoreVersion(update.getVersionCode());
        release();
    }

    @Override
    public void release() {
        this.builder = null;
        this.update = null;
    }
}
