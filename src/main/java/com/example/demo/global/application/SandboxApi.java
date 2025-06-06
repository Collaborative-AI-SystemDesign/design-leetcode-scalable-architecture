package com.example.demo.global.application;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class SandboxApi {

    public String execute(String executableCode, String input) throws IOException, InterruptedException {

        // 1️⃣ Docker 실행에 필요한 임시 디렉토리를 생성한다.
        Path tempDir = Files.createTempDirectory("sandbox");

        // 2️⃣ 사용자 제출 코드를 Solution.java로 임시 디렉토리에 저장한다.
        Path solutionFile = tempDir.resolve("Solution.java");
        Files.writeString(solutionFile, executableCode, StandardCharsets.UTF_8);

        // 3️⃣ Docker run 명령어를 준비한다.
        //    -v: tempDir(호스트) → /app(컨테이너)로 마운트하여 Solution.java 사용
        //    "sandbox-java": Docker 이미지 이름
        //    "/bin/sh -c ..." : bash 쉘에서 javac로 컴파일 후 java로 실행
        ProcessBuilder buildProcessBuilder = new ProcessBuilder(
                "docker", "run", "--rm",
                "--memory=256m", "--cpus=0.5",
                "--network", "none", "--user", "nobody",
                "-v", tempDir.toAbsolutePath() + ":/app",
                "sandbox-java",
                "/bin/sh", "-c", "cd /app && javac Main.java && java Main"
        );

        // 4️⃣ 표준 출력과 표준 에러를 합쳐서 한 번에 출력받도록 설정한다.
        buildProcessBuilder.redirectErrorStream(true);

        // 5️⃣ Docker 컨테이너를 실행한다.
        Process process = buildProcessBuilder.start();

        // 6️⃣ 컨테이너의 표준 입력에 테스트케이스 input을 전달한다.
        //    (즉, 사용자 코드가 Scanner(System.in)으로 입력을 받게끔)
        try (OutputStream os = process.getOutputStream()) {
            os.write(input.getBytes(StandardCharsets.UTF_8)); // input을 바이트로 변환 후 쓰기
            os.flush(); // 버퍼 비우기
        }

        // 7️⃣ 컨테이너의 표준 출력(stdout)을 읽는다.
        String runOutput;
        try (InputStream is = process.getInputStream()) {
            runOutput = new String(is.readAllBytes(), StandardCharsets.UTF_8); // stdout 읽기
        }

        // 3초 타임아웃 걸기
        boolean finished = process.waitFor(3, TimeUnit.SECONDS);
        if (!finished) {
            process.destroyForcibly();
            throw new RuntimeException("Docker run timed out.");
        }

        // 🔟 정상 종료 시 출력 결과를 trim()하여 반환한다.
        return runOutput.trim();

    }

}
