# SpringBoot Monitoring System

> 실시간 애플리케이션 모니터링의 중요성과 필요성을 이해하고,
> SpringBoot 기반 서버에 모니터링 서비스를 실제로 적용해보는 학습 프로젝트

---

## 1. 프로젝트 개요

### 학습 목적
- 서비스 가용성 향상을 위한 모니터링 체계 이해
- 장애 사전 감지 및 대응 프로세스 습득
- 실무에서 사용하는 모니터링 스택 직접 구축 및 운영

### 모니터링 4요소 학습 노트

| 요소 | 설명 | 수집 데이터 예시 |
|------|------|-----------------|
| **Metric** | 시스템/애플리케이션의 성능·사용량을 수치로 표현한 데이터. Prometheus + Grafana로 시각화 | CPU 사용량, 메모리 사용량, HTTP 요청 수, 응답시간, GC 횟수 |
| **Log** | 애플리케이션·시스템에서 발생한 이벤트 기록. Grafana 알럿 시스템으로 이메일 전달 가능 | 오류 로그, 트랜잭션 로그, 시스템 이벤트 로그 |
| **Trace** | 분산 시스템에서 요청이 어떻게 흐르는지 추적하는 정보 | 서비스 간 요청 흐름, API 호출 연결 관계 |
| **Event** | 시스템·애플리케이션에서 발생하는 특정 이벤트 | 사용자 로그인, 데이터베이스 커넥션, 리소스 사용 이벤트 |

### Spring Actuator 학습 노트

> Spring Boot의 운영 정보를 제공하는 도구.
> 애플리케이션 상태 모니터링, Metric 수집, 진단정보를 HTTP 엔드포인트로 노출.

**주요 기능**
1. **헬스체크** — 애플리케이션 및 의존 컴포넌트(DB, 캐시 등)의 상태 확인
2. **메트릭** — JVM, HTTP, 시스템 자원 등 다양한 지표 제공
3. **애플리케이션 정보** — 빌드 버전, Git 커밋 정보 등
4. **환경 설정** — 현재 적용된 프로퍼티 조회

> **보안 주의:** Actuator 엔드포인트는 운영 정보를 노출하므로, 운영 환경에서는 Spring Security로 인증을 적용하는 것이 필수다.

---

## 2. 프로젝트 구조

```
SpringBoot_MonitoringSystem_20260328/
├── src/
│   ├── main/
│   │   ├── java/com/example/demo/
│   │   │   ├── DemoApplication.java       # 애플리케이션 진입점
│   │   │   └── ApiController.java         # 테스트용 REST API 엔드포인트
│   │   └── resources/
│   │       └── application.yml            # Actuator 엔드포인트 설정
│   └── test/
│       └── java/com/example/demo/
│           └── DemoApplicationTests.java  # 통합 테스트
├── docs/
│   ├── test_results/                      # 테스트 결과 누적
│   └── errors/                            # 에러 및 대처법 기록
├── prometheus.yml                         # Prometheus scrape 설정
├── Dockerfile                             # SpringBoot 앱 Docker 이미지
├── docker-compose.yaml                    # 모니터링 스택 통합 실행
└── build.gradle                           # Gradle 의존성 관리
```

### 주요 엔드포인트

| 엔드포인트 | 메서드 | 설명 |
|------------|--------|------|
| `/1` | POST | 테스트 API 1 |
| `/2` | GET | 테스트 API 2 |
| `/actuator/health` | GET | 애플리케이션 헬스체크 |
| `/actuator/metrics` | GET | 메트릭 목록 조회 |
| `/actuator/prometheus` | GET | Prometheus scrape 전용 메트릭 |
| `/actuator/info` | GET | 애플리케이션 정보 |

---

## 3. 데이터베이스 구조

> 현재 이 프로젝트는 DB를 사용하지 않습니다.
> 모니터링 데이터는 Prometheus가 시계열 데이터로 저장합니다.

### Prometheus 데이터 흐름

```
SpringBoot App ──(/actuator/prometheus)──▶ Prometheus ──▶ Grafana
    :8080                                     :9090          :3000
                         (5초마다 scrape)              (시각화/알럿)
```

---

## 4. 사용 기술 및 선택 이유

| 기술 | 버전 | 선택 이유 |
|------|------|-----------|
| **Spring Boot** | 4.0.5 | Java 기반 표준 웹 프레임워크, Actuator 내장으로 모니터링 연동 용이 |
| **Java** | 17 | LTS 버전, Spring Boot 4.x 권장 버전 |
| **Spring Actuator** | Boot 내장 | 코드 변경 없이 메트릭/헬스체크 엔드포인트 즉시 제공 |
| **Micrometer** | Boot 내장 | 벤더 중립적 메트릭 수집 라이브러리, Prometheus 레지스트리 지원 |
| **Prometheus** | latest | Pull 방식 메트릭 수집, 강력한 PromQL 쿼리 언어, 업계 표준 |
| **Grafana** | latest | Prometheus 데이터 시각화, 알럿 기능, 다양한 대시보드 템플릿 |
| **Docker Compose** | 3.8 | 로컬에서 모니터링 스택 전체를 단일 명령으로 실행 가능 |

---

## 5. 설치 및 실행 방법

### 사전 요구사항
- Java 17 이상
- Docker & Docker Compose 설치

### 방법 1: Docker Compose (권장)

```bash
# 1. Gradle 빌드 (JAR 생성)
./gradlew bootJar

# 2. 전체 스택 실행 (SpringBoot + Prometheus + Grafana)
docker-compose up --build

# 3. 각 서비스 접속
# - SpringBoot API:  http://localhost:8080
# - Prometheus:      http://localhost:9090
# - Grafana:         http://localhost:3000 (admin / admin)
```

### 방법 2: 로컬 직접 실행

```bash
# Gradle로 실행
./gradlew bootRun

# 접속
# - SpringBoot API:        http://localhost:8080
# - Actuator 헬스체크:     http://localhost:8080/actuator/health
# - Prometheus 메트릭:     http://localhost:8080/actuator/prometheus
```

### Grafana 초기 설정
1. `http://localhost:3000` 접속 (admin / admin)
2. Data Sources → Add data source → Prometheus
3. URL: `http://prometheus:9090` 입력 후 Save & Test
4. Dashboards → Import → ID `4701` (JVM 대시보드) 입력

---

## 6. 에러 모음

> 발생한 에러와 해결 방법을 누적 기록합니다.
> 상세 내용은 `docs/errors/` 폴더를 참조하세요.

| 날짜 | 에러 | 원인 | 해결 방법 |
|------|------|------|-----------|
| 2026-03-28 | `Datasource ${DS_PROMETHEUS} was not found` | 대시보드 변수 미정의 또는 타입 불일치 | Dashboard settings → Variables → `DS_PROMETHEUS` 변수를 **Datasource** 타입으로 생성, Instance type: Prometheus 지정 |
