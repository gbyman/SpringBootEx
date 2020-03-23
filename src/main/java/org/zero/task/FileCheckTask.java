package org.zero.task;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.zero.domain.BoardAttachVO;
import org.zero.mapper.BoardAttachMapper;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

@Log4j
@Component
public class FileCheckTask {
	// 작업 순서
	// 1. 데이터베이스에서 어제 사용된 파일의 목록을 얻어온다
	// 2. 해당 폴더의 파일 목록에서 데이터베이스에 없는 파일을 찾아낸다
	// 3. 이 후 데이터베이스에 없는 파일들을 삭제한다
	
	@Setter(onMethod_ = { @Autowired })
	private BoardAttachMapper attachMapper;
	
	private String getFolderYesterDay(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		String str = sdf.format(cal.getTime());
		
		return str.replace("-", File.separator);
	}
	
	//Scheduled 어노테이션 내에는 cron이라는 속성을 부여해서 주기를 제어
	// 아래의 cron 설정은 매일 새벽 2시에 실행되도록 설정
	@Scheduled(cron = "0 0 2 * * *")
	public void checkFiles() throws Exception{
		
		log.warn("File Check Task run.......");
		log.warn(new Date());
		
		// file list in database : 어제 날짜로 보관되는 모든 첨부파일 목록 가져온다
		List<BoardAttachVO> fileList = attachMapper.getOldFiles();
		
		// ready for check file in directory with database file list
		// 나중에 비교를 위해 java.nio.Paths의 목록으로 변환
		List<Path> fileListPaths = fileList.stream().map(vo -> Paths.get("C:\\upload", vo.getUploadPath(), vo.getUuid() + "_" + vo.getFileName()))
				.collect(Collectors.toList());
		
		// image file has thumnail file
		// 이미지 파일의 경우 섬네일 파일도 목록에 필요하기 때문에 별도로 처리해서 해당 일의 예상 파일 목록에 추가
		fileList.stream().filter(vo -> vo.isFileType() == true).map(vo -> Paths.get("C:\\upload", vo.getUploadPath(), vo.getUuid() + "s_" + vo.getFileName()))
			.forEach(p -> fileListPaths.add(p));
		
		log.warn("=====================================================");
		
		fileListPaths.forEach(p -> log.warn(p));
		
		// files in yesterday directory
		File targetDir = Paths.get("C:\\upload", getFolderYesterDay()).toFile();
		
		// 실제 폴더에 있는 파일들의 목록에서 데이터 베이스에 없는 파일들을 찾아서 목록으로 준비
		File[] removeFiles = targetDir.listFiles(file -> fileListPaths.contains(file.toPath()) == false);
		
		log.warn("--------------------------------------------------------");
		
		// 전날 등록된 파일들 중에 데이터베이스에서 필요한 파일이 없는 경우에 모든 파일을 찾아서 삭제
		for(File file : removeFiles){
			log.warn(file.getAbsolutePath());
			file.delete();
		}
	}

}
