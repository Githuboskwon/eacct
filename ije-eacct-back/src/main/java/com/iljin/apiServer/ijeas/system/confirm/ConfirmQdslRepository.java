package com.iljin.apiServer.ijeas.system.confirm;


import java.util.List;

public interface ConfirmQdslRepository {

    List<ConfirmDto> getConfirmList(ConfirmDto confirmDto);

}
