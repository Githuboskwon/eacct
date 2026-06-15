package com.iljin.apiServer.ijeas.system.dept;


import java.util.List;

public interface DeptAuthQdslRepository {

    List<DeptAuthDto> getDeptAuthList(DeptAuthDto deptAuthDto);

}
