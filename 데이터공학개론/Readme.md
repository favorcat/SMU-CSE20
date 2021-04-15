# 데이터공학개론     
> 2021학년도 1학기 수강과목
---
1. [Case Study](https://github.com/favorcat/SMU-CSE20/blob/master/데이터공학개론/CaseStudy.md#Case-Study) - 큰 숲을 본다
    - [용어정리](https://github.com/favorcat/SMU-CSE20/blob/master/데이터공학개론/CaseStudy.md#용어정리)
    - [사업의 목적](https://github.com/favorcat/SMU-CSE20/blob/master/데이터공학개론/CaseStudy.md#사업의-목적)
    - [1. Subway Transfer System 개선](https://github.com/favorcat/SMU-CSE20/blob/master/데이터공학개론/CaseStudy.md#11-Subway-Transfer-System-개선)
    - [2. Link Analysis 활용 분석 모델](https://github.com/favorcat/SMU-CSE20/blob/master/데이터공학개론/CaseStudy.md#12-Link-Analysis-활용-분석-모델)
    - [3. 전통적인 수요예측 분석 모델](https://github.com/favorcat/SMU-CSE20/blob/master/데이터공학개론/CaseStudy.md#13-전통적인-수요예측-분석-모델)
        - [시계열분석](https://github.com/favorcat/SMU-CSE20/blob/master/데이터공학개론/CaseStudy.md#132-시계열분석)
    - [4. 제조 공정 모델](https://github.com/favorcat/SMU-CSE20/blob/master/데이터공학개론/CaseStudy.md#14-제조-공정-모델)
        - [금융](https://github.com/favorcat/SMU-CSE20/blob/master/데이터공학개론/CaseStudy.md#142-금융)
    - [5. 유통 관련 분석 사례](https://github.com/favorcat/SMU-CSE20/blob/master/데이터공학개론/CaseStudy.md#15-유통-관련-분석-사례)
    - [6. 금융 관련 분석 사례](https://github.com/favorcat/SMU-CSE20/blob/master/데이터공학개론/CaseStudy.md#16-금융-관련-분석-사례)
2. [Analystic Methodology](https://github.com/favorcat/SMU-CSE20/blob/master/데이터공학개론/AnalysisMethodology.md) - 분석방법론(전통적인/수학적인 분석방법)
    - [What is Clustering?](https://github.com/favorcat/SMU-CSE20/blob/master/데이터공학개론/AnalysisMethodology.md#1-what-is-clustering)
        - [1.1 Predictive vs Descriptive Analysis](https://github.com/favorcat/SMU-CSE20/blob/master/데이터공학개론/AnalysisMethodology.md#11-predictive-vs-descriptive-analysis)
    - Partitional Clustering : K-means Clustering
    - Hierarchical Clustering
    - Association Rule
    - What is Dimensionality Reduction?
    - Principle Component Analysis (PCA)
3. Mining - 분석(2번과 다른 분석 방법)
4. 비정형 (Text)
5. SNA
---

### 용어정리

* Staff Resistance - 기존 직원들의 저항감
* Atypical - 비정형적인
* Forecasting - 예측
* Optimization - 최적화
  (ex. 네비게이션 시스템, Logictics - 물류 (가장 효율적으로 많이 담는 것))
* Scarcity - 희소성
* Recommender System - 수학적인 공식에 의해서 결정되는 결과
* Customer Behavior - 소비자 행동 특성
* Random Sampling - 진짜 랜덤은 아닌 랜덤 샘플링
* Stratified Random Sampling - 층화 무작위 표본추출
* [R/O](https://github.com/favorcat/SMU-CSE20/blob/master/데이터공학개론/CaseStudy.md#RO) → Repair Order 고객의 수리정보
* [분석](https://github.com/favorcat/SMU-CSE20/blob/master/데이터공학개론/CaseStudy.md#RO) -> 객관적인 데이터를 통해 주관적인 판단을 유추한다.
* [Auto-Regression](https://github.com/favorcat/SMU-CSE20/blob/master/데이터공학개론/CaseStudy.md#시계열분석) - 자기회귀, 자기상관
* [Stationary Test](https://github.com/favorcat/SMU-CSE20/blob/master/데이터공학개론/CaseStudy.md#stage2) - 정상성 테스트
* [Unit Root Test](https://github.com/favorcat/SMU-CSE20/blob/master/데이터공학개론/CaseStudy.md#stage2) - 단위근 검정 (Dickey Fuller가 발견, t기 t-k기까지 파고 들어간다.)
* [Granger Causality Test](https://github.com/favorcat/SMU-CSE20/blob/master/데이터공학개론/CaseStudy.md#stage3) - 그랜저 인과관계 검증
* [GIRFs](https://github.com/favorcat/SMU-CSE20/blob/master/데이터공학개론/CaseStudy.md#stage4) - 일반화된 충격반응함수 (Generalized Impluse Response Functions)
* [Wear-in Time](https://github.com/favorcat/SMU-CSE20/blob/master/데이터공학개론/CaseStudy.md#stage4) - 시계열적으로 얼마나 빠른가(소요시간)
* [GFEVD](https://github.com/favorcat/SMU-CSE20/blob/master/데이터공학개론/CaseStudy.md#stage-5--stage-4) - 일반화된 예측오차 분산분해 (Generalized Forecast Error Variance Decomposition)
* [Prospect Theory](https://github.com/favorcat/SMU-CSE20/blob/master/데이터공학개론/CaseStudy.md#보험-금액을-산정하는-기준) - 기대이론(츠버스키)
* [Movement Pattern Analysis](https://github.com/favorcat/SMU-CSE20/blob/master/데이터공학개론/CaseStudy.md#15-유통-관련-분석-사례) - 동선 분석
* [Triangulation](https://github.com/favorcat/SMU-CSE20/blob/master/데이터공학개론/CaseStudy.md#15-유통-관련-분석-사례) - 삼각 측량
* [Dead reckoning](https://github.com/favorcat/SMU-CSE20/blob/master/데이터공학개론/CaseStudy.md#15-유통-관련-분석-사례) - 추측
* [Predictive](https://github.com/favorcat/SMU-CSE20/blob/master/데이터공학개론/AnalysisMethodology.md#1-what-is-clustering) - 예측적
* [Descriptive](https://github.com/favorcat/SMU-CSE20/blob/master/데이터공학개론/AnalysisMethodology.md#1-what-is-clustering) - 서술적