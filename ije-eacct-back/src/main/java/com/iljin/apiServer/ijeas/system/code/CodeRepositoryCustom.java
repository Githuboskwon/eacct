package com.iljin.apiServer.ijeas.system.code;

import java.util.List;

public interface CodeRepositoryCustom {

    List<CodeDto> getGroupCodeDetailList(CodeDto codeDto);
}
