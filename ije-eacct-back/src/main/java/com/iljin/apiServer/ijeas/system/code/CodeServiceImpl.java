package com.iljin.apiServer.ijeas.system.code;

import com.iljin.apiServer.core.security.user.User;
import com.iljin.apiServer.core.util.Pair;
import com.iljin.apiServer.core.util.Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class CodeServiceImpl implements CodeService {

	private final CodeHeaderRepository codeHeaderRepository;
	private final CodeDetailRepository codeDetailRepository;
	private final CodeQdslRepository codeQdslRepository;
	private final Util util;

	@Override
	public List<CodeDetail> getCodeDetailAll() {
		return codeDetailRepository.findAll();
	}

	@Override
	public List<Pair> getComboBox(CodeDto codeDto) {
		return codeQdslRepository.getUsedComboByCodeDto(codeDto);
	}

	@Override
	public List<Pair> getComboSeq(CodeDto codeDto) {
		return codeQdslRepository.getUsedComboSeqByCodeDto(codeDto);
	}

	@Override
	public List<Map> getByCodeDto(CodeDto codeDto) {
		if(StringUtils.isEmpty(codeDto.getRemark3())) {
			return codeQdslRepository.findByGroupCd(codeDto.getCompCd(), codeDto.getGroupCd());
		} else {
			return codeQdslRepository.findByGroupCdAndRemark3(codeDto.getCompCd(), codeDto.getGroupCd(), codeDto.getRemark3());
		}
	}

	@Override
	public List<Map> getCodeAll() {
		return codeQdslRepository.getCodeAll();
	}

	@Override
	public List<CodeDto> getGroupCodeList(CodeDto codeDto) {
		return codeQdslRepository.getGroupCodeList(codeDto);
	}

	@Override
	public Page<CodeDto> getGroupCodeListPage(CodeDto codeDto, Pageable pageable) {
		return codeQdslRepository.searchGroupCodePageSimple(codeDto, pageable);
	}

	@Override
	public List<CodeDto> getGroupCodeDetailList(CodeDto codeDto) {
		return codeQdslRepository.getGroupCodeDetailList(codeDto);
	}

	@Override
	public ResponseEntity<String> saveCodeLists(CodeHeaderDetails codeHeaderDetails) {
		User loginUser = util.getLoginUser();
		String loginId = loginUser.getLoginId();
		String compCd = loginUser.getCompCd();
		//String loginId = "admin";
		//String compCd = "101600";

		List<CodeDto> codeHeaders = codeHeaderDetails.getCodeHeader();
		List<CodeDto> codeDetails = codeHeaderDetails.getCodeDetail();

		/* 그룹코드 영역(헤더) 처리 */
		for (CodeDto header : codeHeaders) {
			String groupCd = header.getGroupCd();
			String codeCompCd = header.getCompCd();

			CodeHeaderKey codeHeaderKey = new CodeHeaderKey();
			codeHeaderKey.setGroupCd(groupCd);
			codeHeaderKey.setCompCd(codeCompCd);

			Optional<CodeHeader> codeHeader = codeHeaderRepository.findById(codeHeaderKey);

			if (codeHeader.isPresent()) {
				/* update */
				codeHeader.ifPresent(c -> {
					c.updateCodeHeader(
							header.getGroupNm(),
							header.useYn,
							header.groupDesc
					);

					codeHeaderRepository.save(c);
				});
			} else {
				/* new Insert */
				CodeHeader c = new CodeHeader().builder()
						.groupCd(header.getGroupCd())
						.compCd(header.getCompCd())
						.groupNm(header.getGroupNm())
						.groupDesc(header.getGroupDesc())
						.useYn(header.getUseYn())
						.build();
				/*try {
					PropertyUtils.copyProperties(c, header);
				} catch (Exception e) {
					e.printStackTrace();
				}*/
				codeHeaderRepository.save(c);
			}
		}

		/*
		 * 상세코드 영역(상세) 처리 Desc. 해당 회사코드/그룹코드로 전체 삭제 후 새로 추가
		 */
		String detailGroupCd = codeDetails.get(0).groupCd;
		codeDetailRepository.deleteByCompCdAndGroupCd(compCd, detailGroupCd);
		for (CodeDto detail : codeDetails) {
			CodeDetail c = new CodeDetail().builder()
					.groupCd(detail.getGroupCd())
					.detailCd(detail.getDetailCd())
					.compCd(detail.getCompCd())
					.detailNm(detail.getDetailNm())
					.useYn(detail.getUseYn())
					.orderSeq(detail.getOrderSeq())
					.detailDesc(detail.getDetailDesc())
					.remark1(detail.getRemark1())
					.remark2(detail.getRemark2())
					.remark3(detail.getRemark3())
					.remark4(detail.getRemark4())
					.remark5(detail.getRemark5())
					.build();
			/*try {
				PropertyUtils.copyProperties(c, detail);
			} catch (Exception e) {
				e.printStackTrace();
			}*/

			codeDetailRepository.save(c);
		}

		return new ResponseEntity<>("저장되었습니다.", HttpStatus.OK);
	}

	@Override
	public ResponseEntity<String> deleteCode(String groupCd) {
		String compCd = util.getLoginCompCd();
		//String compCd = "101600";

		/* delete Detail codes */
		codeDetailRepository.deleteByCompCdAndGroupCd(compCd, groupCd);

		/* delete Header Code */
		CodeHeaderKey codeHeaderKey = new CodeHeaderKey();
		codeHeaderKey.setCompCd(compCd);
		codeHeaderKey.setGroupCd(groupCd);
		codeHeaderRepository.deleteById(codeHeaderKey);

		return new ResponseEntity<>("삭제되었습니다.", HttpStatus.OK);
	}

	@Override
	public Optional<CodeDetail> getGroupCodeDetail(CodeDto codeDto) {
		return codeDetailRepository.findByCompCdAndGroupCdAndUseYnAndDetailCd(codeDto.getCompCd(), codeDto.getGroupCd(), "Y", codeDto.getDetailCd());
	}

}
