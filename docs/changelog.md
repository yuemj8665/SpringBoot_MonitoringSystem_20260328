# 변경 로그

> 프로젝트의 주요 변경 사항을 기록합니다.

---

## [2026-03-28]

### 추가
- 프로젝트 초기 구성: Spring Boot 4.0.5 + Actuator + Micrometer Prometheus
- Docker Compose 모니터링 스택 구성 (SpringBoot + Prometheus + Grafana)
- ApiController: 테스트용 엔드포인트 POST /1, GET /2 추가
- application.yml: Actuator 엔드포인트 4개 노출 (health, info, metrics, prometheus)
- Readme.md 문서 정비 (CLAUDE.md 규칙 준수, 기존 학습 노트 보존 및 포맷 개선)
- docs/ 폴더 구조 생성 (errors/, test_results/, changelog.md)
- process_personal.md 생성 (신입~주니어 개발자 대상 상세 설명)
- .gitignore에 CLAUDE.md, process_personal.md 추가
