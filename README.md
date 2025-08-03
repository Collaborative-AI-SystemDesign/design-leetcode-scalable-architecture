# LeetCode 클론 플랫폼 - 성능 최적화 프로젝트

## 📖 프로젝트 개요

이 프로젝트는 **시스템 디자인 인터뷰 준비를 위한 실무 중심 학습 프로젝트**입니다. LeetCode와 같은 온라인 코딩 플랫폼을 직접 설계하고 구현하며, 성능 병목을 찾아내고 구조적으로 해결하는 과정을 통해 대규모 시스템 설계 원리를 체득하는 것이 목표입니다.

단순히 동작하는 서비스를 만드는 것이 아니라, **캐싱 전략, 페이지네이션 방식 선택, 코드 실행 샌드박스 분리, 실시간 리더보드 설계, 메시지 큐와 컨테이너를 활용한 비동기 처리 구조** 등 시스템 디자인 인터뷰에서 자주 등장하는 주제들을 실제로 설계하고 실험한 프로젝트입니다.

> 📚 **참고 자료**:
- [LeetCode 프로젝트 상세 문서](https://wonjoon.gitbook.io/joons-til/k6-optimization-project/leetcode-project)
- [Implementation](https://wonjoon.gitbook.io/joons-til/k6-optimization-project/leetcode-project/implementation)
- [Requirement](https://wonjoon.gitbook.io/joons-til/k6-optimization-project/leetcode-project/implementation)
- [API endpoint](https://wonjoon.gitbook.io/joons-til/k6-optimization-project/leetcode-project/implementation/api-endpoints)
- [Optimization: GET ProblemList](https://wonjoon.gitbook.io/joons-til/k6-optimization-project/leetcode-project/optimization1-java-application/get-problemlist)
- [Optimization: GET ProblemDetail](https://wonjoon.gitbook.io/joons-til/k6-optimization-project/leetcode-project/optimization1-java-application/get-problemdetail)
- [Optimization: POST Submit Problem](https://wonjoon.gitbook.io/joons-til/k6-optimization-project/leetcode-project/optimization1-java-application/post-submit-problem)
- [Optimization: GET LeaderBoard](https://wonjoon.gitbook.io/joons-til/k6-optimization-project/leetcode-project/optimization1-java-application/get-leaderboard)
- [Load Test: Apis](https://wonjoon.gitbook.io/joons-til/k6-optimization-project/leetcode-project/k6-load-test/load-test-apis)
- [Load Test: POST Submit Problem](https://wonjoon.gitbook.io/joons-til/k6-optimization-project/leetcode-project/k6-load-test/load-test-submission-api)
- [TroubleShooting: JPA OSIV](https://wonjoon.gitbook.io/joons-til/k6-optimization-project/leetcode-project/troubleshooting/jpa-osiv)
- [RabbitMq: ackMode = None](https://wonjoon.gitbook.io/joons-til/k6-optimization-project/leetcode-project/troubleshooting/rabbitmq-ackmode-none)
- [Throuput beyond #vCPU](https://wonjoon.gitbook.io/joons-til/k6-optimization-project/leetcode-project/troubleshooting/throughput-beyond-vcpu)

## 🛠️ 기술 스택

### Backend
- **Framework**: Spring Boot 3.5.0
- **Language**: Java 17
- **Build Tool**: Gradle 8.x
- **Database**: MySQL 8.0
- **Cache**: Redis
- **Message Queue**: RabbitMQ

### Infrastructure & DevOps
- **Monitoring**: Prometheus + Grafana
- **Database Migration**: SQL Scripts

### Testing & Performance
- **Performance Monitoring**: Spring Boot Actuator + Micrometer

## 🚀 주요 기능

### 1. 문제 관리 시스템
- **문제 목록 조회**: 다양한 페이징 방식 지원 (Cursor, Offset)
- **문제 상세 조회**: 캐싱 전략 적용으로 성능 최적화
- **난이도별 분류**: Easy, Medium, Hard
- **카테고리별 분류**: Array, String, Tree 등

### 2. 코드 제출 및 실행
- **안전한 코드 실행**: Docker 샌드박스 환경 분리
- **비동기 처리**: RabbitMQ를 통한 제출 처리
- **실시간 결과 반환**: 메시지 큐 기반 결과 처리

### 3. 리더보드 시스템
- **실시간 순위**: Redis 기반 고성능 리더보드
- **콘테스트별 순위**: 대회 단위 순위 관리
- **성능 최적화**: 캐싱을 통한 빠른 응답

### 4. 사용자 관리
- **JWT 인증**: 토큰 기반 인증 시스템
- **사용자 통계**: 제출 이력 및 성과 추적

## 시스템 디자인
<img width="1009" height="775" alt="image" src="https://github.com/user-attachments/assets/c11fb70c-75e6-4d29-a929-da5065c14bef" />

- API Server: This.
  
- Code Sandbox: https://github.com/Collaborative-AI-SystemDesign/leetcode-sandbox
  
- Result Server: https://github.com/Collaborative-AI-SystemDesign/leetcode-sandbox-consumer


## 📊 성능 최적화 현황

### 1. 데이터베이스 최적화
- **JPA 설정**: `spring.jpa.open-in-view: false`로 OSIV 비활성화
- **배치 처리**: `hibernate.jdbc.batch_size: 1000` 설정
- **인덱싱**: 효율적인 쿼리를 위한 데이터베이스 인덱스 최적화

### 2. 캐싱 전략
- **Redis 캐시**: 문제 상세 정보 및 리더보드 캐싱
- **Spring Cache**: 애플리케이션 레벨 캐싱 적용

### 3. 비동기 처리
- **RabbitMQ**: 코드 실행 요청의 비동기 처리
- **메시지 큐 최적화**: `acknowledge-mode: none` 설정으로 성능 향상

### 4. 서버 최적화
- **Tomcat 튜닝**: max-threads: 250, max-connections: 10000
- **Connection Pool**: 효율적인 데이터베이스 연결 관리

## 🎯 API 엔드포인트

### 문제 관리
```http
# 문제 목록 조회 (범위 기반)
GET /problems?start={start}&end={end}

# 문제 목록 조회 (Offset 페이징)
GET /problems/offset?page={page}&size={size}

# 문제 목록 조회 (Cursor 페이징)
GET /problems/cursor?cursor={cursor}&limit={limit}

# 문제 상세 조회
GET /problems/{problemId}

# 코드 제출
POST /problems/{problemId}/submission
Content-Type: application/json
{
  "code": "public class Solution {...}",
  "language": "JAVA"
}
```

### 리더보드
```http
# 콘테스트 리더보드 조회
GET /v1/contests/{contest_id}/leaderboard
```

## 🗄️ 데이터베이스 스키마

### 주요 테이블
- **problem**: 문제 정보 (제목, 설명, 난이도, 제약사항)
- **submission**: 제출 내역 (코드, 언어, 상태, 실행시간, 메모리)
- **user**: 사용자 정보
- **contest**: 대회 정보
- **leaderboard**: 리더보드 순위
- **testcases**: 테스트케이스 (문제당 100개)
- **example**: 예제 (문제당 3개)
- **starter_code**: 시작 코드 템플릿

## 🚀 시작하기

### 1. 환경 설정

#### 필수 요구사항
- Java 17+
- Docker & Docker Compose
- Gradle 8.x

#### 공통 환경 변수 설정
```bash
export MYSQL_HOST=localhost
export MYSQL_DB=sd_db
export MYSQL_USER=sd_user
export MYSQL_PASSWORD=sd_password
export REDIS_HOST=localhost
export RABBITMQ_HOST=localhost
export RABBITMQ_USERNAME=guest
export RABBITMQ_PASSWORD=guest
```

## 📊 모니터링

### 애플리케이션 메트릭
- **Endpoint**: `http://localhost:8080/actuator`
- **Prometheus 메트릭**: `http://localhost:8080/actuator/prometheus`

### Grafana 대시보드
- **URL**: `http://localhost:3000`
- **기본 계정**: admin/admin

## 🔧 성능 최적화 기법

### 1. 데이터베이스 성능
- **페이지네이션**: Cursor vs Offset 방식 성능 비교
- **JPA 최적화**: OSIV 비활성화, 배치 처리 적용
- **쿼리 최적화**: N+1 문제 해결, 인덱스 활용

### 2. 캐싱 전략
- **리더보드 캐싱**: Redis Sorted Set 활용
- **문제 상세 캐싱**: 조회 성능 향상
- **적절한 TTL 설정**: 메모리 사용량과 성능 밸런스

### 3. 비동기 처리
- **메시지 큐**: 코드 실행의 비동기 처리
- **RabbitMQ 최적화**: ACK 모드 조정으로 처리량 향상

## 📈 성능 테스트 결과

### API 성능
- **문제 목록 조회**: 초당 1000+ 요청 처리
- **문제 상세 조회**: 캐싱 적용으로 응답시간 50ms 이하
- **코드 제출**: 비동기 처리로 즉시 응답

### 시스템 리소스
- **메모리 사용량**: 힙 메모리 최적화
- **데이터베이스 연결**: 커넥션 풀 효율화
- **캐시 적중률**: 90% 이상 캐시 활용

## 🚧 트러블슈팅

### 주요 해결 이슈
1. **JPA OSIV 문제**: LazyInitializationException 해결
2. **RabbitMQ ACK 모드**: 성능 vs 안정성 트레이드오프
3. **vCPU 처리량 한계**: 스레드 풀 최적화

---

**📌 프로젝트 목적**: 시스템 디자인 인터뷰 대비 실무 경험 쌓기  
**🎯 핵심 학습 내용**: 대규모 시스템 설계, 성능 최적화, 부하 테스트  
**📊 성과**: 실제 운영 환경 수준의 성능 최적화 경험 습득
