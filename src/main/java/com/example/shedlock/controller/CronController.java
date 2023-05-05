package com.example.shedlock.controller;

import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;

@Controller
@Slf4j
public class CronController {

	@PostConstruct
	void postConstruct(){
		System.out.println("@@Test - CronController.postConstruct");
		log.info("@@test - PostConstruct");
	}


	/**
	 * 해당 method 가 처음 실행되는 시점에
	 * locked_at = 시작시간
	 * lock_until = 시작시간 + lockAtMostFor 로 INSERT
	 * (서버 초기화 이후 처음 실행될때만 INSERT 이후 실행될때는 INSERT 가 아닌 UPDATE 로 수행됨)
	 * (INSERT 중에 이미 row 가 있어서 DuplicateKeyException 발생 시 UPDATE 로 수행된다 )
	 *
	 * (UPDATE 문의 WHERE 조건에 의해서 이미 다른 서버에서 실행된 이력이 있는지 여부가 확인 되는데
	 * WHERE 문 조건에 의해 effected row 수가 0 이면 해당 method 는 실행되지 않는다.
	 *
	 * 종료되는 시점에
	 * lock_until = 시작시간 + lockAtLeastFor 로 UPDATE
	 *
	 * P.S) lockAtMostFor 는 lockAtLeastFor 보다 커야한다.
	 *
	 */
	@Scheduled(cron = "0 0/2 * * * *") // 5분 마다 실행
	@SchedulerLock(name = "CronController_scheduledTask" // Unique 해야하며 Class명_method명 정도면 무난하다.
			, lockAtLeastFor = "PT100S" // 중복수행 방지 30초
			, lockAtMostFor = "PT110S" // 죽었을때를 대비 50초
			// TODO : 최적화. PKI 서버 87초 느림
	)
	public void scheduledTask(){
		String serverName = getServerName();
		log.info("===============CronController.scheduledTas Start [serverName={}]======================", serverName);
		System.out.println("===============CronController.scheduledTas Start [serverName={}]======================"+ serverName);
		try {
			Thread.sleep(10_000L);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}

		System.out.println("===============CronController.scheduledTas End [serverName={}]======================"+ serverName);
		log.info("===============CronController.scheduledTas End   [serverName={}]======================", serverName);
	}


	private static boolean isWindowOs() {
		return (System.getProperty("os.name").contains("Windows") );
	}


	private static String getServerName() {
		if ( isWindowOs() ) {
			return System.getProperty("os.name");
		}else {
			return System.getProperty("jboss.server.name"); // dreambatch01 , dreambatch02
		}
	}




}
