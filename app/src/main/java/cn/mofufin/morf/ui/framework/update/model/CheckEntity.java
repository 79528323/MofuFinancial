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
package cn.mofufin.morf.ui.framework.update.model;

import java.util.HashMap;
import java.util.Map;

import cn.mofufin.morf.ui.framework.update.UpdateConfig;
import cn.mofufin.morf.ui.framework.update.business.UpdateWorker;

/**
 * 配置的更新api实体类。此实体类将被{@link UpdateWorker}进行使用。
 * <p>
 * <p>配置方式：通过{@link UpdateConfig#checkEntity(CheckEntity)}对复杂api数据进行定制，或者通过{@link UpdateConfig#url(String)}对简单GET请求的更新api进行定制。
 *
 * @author haoge
 */
public class CheckEntity {
    private String method = "GET";
    private String url;
    private Map<String, String> params;

    public String getMethod() {
        return method;
    }

    public CheckEntity setMethod(String method) {
        this.method = method;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public CheckEntity setUrl(String url) {
        this.url = url;
        return this;
    }

    public Map<String, String> getParams() {
        if (params == null) {
            params = new HashMap<>();
        }
        return params;
    }

    public CheckEntity setParams(Map<String, String> params) {
        this.params = params;
        return this;
    }
}
