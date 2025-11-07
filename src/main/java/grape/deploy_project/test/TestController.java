package grape.deploy_project.test;

import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestController {

    @GetMapping
    public String test() {
        // 최소 대기 시간 (200ms = 0.2초)
        final int MIN_WAIT_MS = 200;
        // 최대 대기 시간 (1500ms = 1.5초)
        final int MAX_WAIT_MS = 1500;

        // 랜덤 객체 생성
        Random random = new Random();

        // MIN_WAIT_MS와 MAX_WAIT_MS 사이의 랜덤 시간(밀리초) 계산
        // 1500 - 200 = 1300. 범위는 0 ~ 1300(포함)
        int randomWaitMs = random.nextInt(MAX_WAIT_MS - MIN_WAIT_MS + 1) + MIN_WAIT_MS;

        // 대기 시작 전 시간 기록 (선택 사항)
        // long startTime = System.currentTimeMillis();

        try {
            // 계산된 시간(밀리초)만큼 현재 스레드를 대기(잠재우기)시킵니다.
            Thread.sleep(randomWaitMs);
        } catch (InterruptedException e) {
            // 스레드가 대기 중 인터럽트되면 현재 스레드의 인터럽트 상태를 다시 설정합니다.
            Thread.currentThread().interrupt();
            // 오류 발생 시 대기 시간을 0으로 처리하거나 다른 오류 메시지를 반환할 수 있습니다.
            return "대기 중 인터럽트 발생";
        }

        // 밀리초를 초 단위로 변환하여 문자열로 포맷
        // 예: 750ms -> 0.750초
        double waitTimeSeconds = (double) randomWaitMs / 1000.0;

        // 반환할 문자열 포맷: 소수점 셋째 자리까지 표시
        return String.format("%.3f초", waitTimeSeconds);
    }
}
