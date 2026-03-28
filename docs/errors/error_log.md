# 에러 로그

> 프로젝트에서 발생한 에러와 대처 방법을 기록합니다.

---

| 날짜 | 에러 메시지 | 원인 | 해결 방법 |
|------|-------------|------|-----------|
| 2026-03-28 | `Datasource ${DS_PROMETHEUS} was not found` | 대시보드 내 `${DS_PROMETHEUS}` 변수가 미정의되었거나 타입 불일치 | 대시보드 Variables 설정에서 `DS_PROMETHEUS` 변수를 Datasource 타입으로 생성, Instance type을 Prometheus로 지정 |

---

## 상세 기록

### [2026-03-28] Datasource ${DS_PROMETHEUS} was not found

**현상**
Prometheus 데이터 소스 등록 및 연결 테스트는 성공했으나, Import한 대시보드의 개별 패널들이 데이터를 불러오지 못하고 위 에러 메시지를 출력함.

**원인 (Root Cause)**
- 대시보드 내부에서 데이터 소스를 동적으로 선택하기 위해 `${DS_PROMETHEUS}` 변수를 사용하도록 설계되어 있으나, Grafana 설정에 해당 이름의 `Datasource` 타입 변수가 존재하지 않거나 잘못된 타입(예: Query 타입)으로 생성되어 매핑이 깨짐
- 외부에서 가져온 대시보드 JSON이 특정 변수명을 참조하지만, 실제 등록한 데이터 소스 명칭과 불일치하는 전형적인 구성 오류

**해결 방법 (Solution)**

1. **대시보드 설정 진입**
   - 에러 발생 대시보드 우측 상단 `Edit` 버튼 또는 톱니바퀴 아이콘(Dashboard settings) 클릭
   - 단축키: `d` + `s`

2. **Variables 탭 이동**
   - 왼쪽 사이드바에서 `Variables` 탭 선택

3. **변수 생성 및 수정**
   - `Variable type`: **Datasource** 로 설정 (Query 타입 아님)
   - `Name`: **DS_PROMETHEUS** 입력
   - `Instance type`: **Prometheus** 선택

4. **적용 및 저장**
   - `Apply` 버튼 클릭 → 대시보드 상단 셀렉트 박스에서 Prometheus 데이터 소스 선택
   - 모든 패널 정상 출력 확인
