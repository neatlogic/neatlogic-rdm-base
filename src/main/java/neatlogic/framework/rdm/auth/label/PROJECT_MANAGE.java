/*Copyright (C) 2024  深圳极向量科技有限公司 All Rights Reserved.

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.*/

package neatlogic.framework.rdm.auth.label;

import neatlogic.framework.auth.core.AuthBase;
import neatlogic.framework.util.$;

import java.util.Collections;
import java.util.List;

public class PROJECT_MANAGE extends AuthBase {
    @Override
    public String getAuthDisplayName() {
        return $.t("nfral.project_manage.getauthdisplayname");
    }

    @Override
    public String getAuthIntroduction() {
        return $.t("nfral.project_manage.getauthintroduction");
    }

    @Override
    public String getAuthGroup() {
        return "rdm";
    }

    @Override
    public Integer getSort() {
        return 2;
    }

    @Override
    public List<Class<? extends AuthBase>> getIncludeAuths() {
        return Collections.singletonList(RDM_BASE.class);
    }
}
