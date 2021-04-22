## 2. Analysis Methodology
- [2. Analysis Methodology](#2-analysis-methodology)
  - [용어정리](#용어정리)
  - [1. What is Clustering](#1-what-is-clustering)
    - [1.1 Predictive vs Descriptive Analysis](#11-predictive-vs-descriptive-analysis)
    - [1.2 What is Clustering](#12-what-is-clustering)
      - [1.2.1 Clustering(군집분석)](#121-clustering군집분석)
      - [1.2.2 Clustering vs. PCA](#122-clustering-vs-pca)
      - [1.2.3 Clustering의 적용 사례](#123-clustering의-적용-사례)
      - [1.2.4 Clustering Methodologies](#124-clustering-methodologies)
      - [1.2.5 군집분석](#125-군집분석)
    - [1.3 Practical Issues](#13-practical-issues)
      - [1.3.1 Small Decisions with Big Consequences](#131-small-decisions-with-big-consequences)
      - [1.3.2 Other Considerations in Clustering](#132-other-considerations-in-clustering)
  - [2. K-means Clustering](#2-k-means-clustering)
    - [2.1 What is K-means Clustering?](#21-what-is-k-means-clustering)
      - [2.1.1 K-Means Clustering(K-평균 군집)](#211-k-means-clusteringk-평균-군집)
      - [2.1.2 K-Means Clustering의 성질](#212-k-means-clustering의-성질)
      - [2.1.3 K-Means Clustering의 목적](#213-k-means-clustering의-목적)
    - [2.2 How to Find K?](#22-how-to-find-k)
    - [2.3 K-medoids](#23-k-medoids)
      - [2.3.1 Mean vs. Median](#231-mean-vs-median)
  - [3. Hierarchical Clustering](#3-hierarchical-clustering)
    - [3.1 What is Hierarchical Clustering?](#31-what-is-hierarchical-clustering)
      - [3.1.1 Hierarchical Clustering (계층적 군집)](#311-hierarchical-clustering-계층적-군집)
      - [3.1.2 Hierarchical Clustering은 항상 Partitional Clustering보다 좋을까?](#312-hierarchical-clustering은-항상-partitional-clustering보다-좋을까)
    - [3.2 Interpreting a Dendrogram](#32-interpreting-a-dendrogram)
      - [3.2.1 연결의 종류와 방법](#321-연결의-종류와-방법)
### 용어정리
* [Predictive](#1-what-is-clustering) - 예측적
* [Descriptive](#1-what-is-clustering) - 서술적
* [Clustering](#12-what-is-clustering) - 군집분석
* [Over-fitting](#12-what-is-clustering) - 과적합
* [Under-fitting](#12-what-is-clustering) - 과소적합, 소홀한 적합
* [Principal Component Analysis](#12-what-is-clustering) - 주성분 분석
* [Partitional Clustering](#124-Clustering-Methodologies) - 분할적 군집
* [Hierarchical Clustering](#124-Clustering-Methodologies) - 계층적 군집
* [Agglomerative method](#124-Clustering-Methodologies) - 한 군집 내 부분 군집을 허용하는 방법, N개의 데이터를 각각 하나의 군집으로 간주하고 데이터의 특성이 가까운 군집끼리 순차적으로 합해 나가는 병합적 방법
* [Euclidean Distance](#131-small-decisions-with-big-consequences) - 유클리디안 거리
* [Outlier](#132-other-considerations-in-clustering) - 이상치
* [Squared Euclidean Distance](#213-k-means-clustering의-목적) - 유클리드 거리 제곱

## 1. What is Clustering
기반하에서 뭔가를 만든다 - [Descriptive](#용어정리)    
내 주관적인 기준 하에서 개발한다 - [Predictive](#용어정리)

### 1.1 Predictive vs Descriptive Analysis
자극이 와야 반응을 한다.    
자극(X), 설명변수 == 독립변수(independent), 원인변수 <-> 반응변수 == 종속변수(dependent)(Y), 결과변수    
    
### 1.2 What is Clustering
인과관계에 대한 분석을 시행할 때도, 그 전에 꼭 봐야하는 분석시도가 있다.    
![CodeCogsEqn](https://user-images.githubusercontent.com/49950126/114656693-7d84b080-9d29-11eb-8bc7-fde2a9ad4cbb.png)과 ![CodeCogsEqn (2)](https://user-images.githubusercontent.com/49950126/114656707-85445500-9d29-11eb-8673-d1910f6a3f18.png)를 사전에 검사를 해봤더니, 다중공선성이 높게 나타났다.    
- 교집합 부분이 많다.
- 기준에 의해서 한 개의 x변수를 빼야할 경우도 발생한다.
- 차원을 늘릴 필요가 없다.
- 차원 축소를 시행한다.
- 설명력이 조금 늘어난다고 해서 좋은 것은 아니다.
- 오히려 x변수가 적으면 훨씬 좋다.
- x들과 y의 인과관계를 보는게 분석의 목적인데, 그 전에 x들의 상관관계를 먼저 보게 된다.
- 다중공선성을 보면서 차원 축소를 유도한다.
- 묶어줄 때 기준이 필요하다.
    
K-means cluster - K값을 관측자 스스로 설정하게 되어있다.
    
Clustering은 주어진 자료 특성을 통해 클러스터를 찾는 다양한 방법을 통칭    
이때 얻어진 클러스터 내 관측치는 동질적이고 클러스터 간 관측치는 이질적인 특성을 갖는다.    
### 1.2.1 Clustering(군집분석)
- 자료의 특성을 이용해 그 자료가 갖고 있는 하위 그룹 또는 클러스터를 찾는 다양한 방법
- 이때 각 그룹 내 관측치들은 서로 동질적이고 각 그룹 간 관측치들은 이질적으로 나뉘는 것을 목표로 한다.
  (Over-fitting, Under-fitting의 문제 가능) -> 어디까지가 오버, 언더, 적절한 적합일까?
       
### 1.2.2 Clustering vs. PCA
- 공통점
    - 주어진 데이터를 기술적으로 요약하는 것을 목적으로 함
- 차이점(목적을 달성하는 방법에서 차이)
    - Clustering은 관측치 혹은 변수들의 유사성 기반으로 동질적인 집단으로 묶는 방법
    - PCA는 주어진 데이터의 특성을 잘 나타내는 저차원의 선형결합을 찾는 방법(차원축소를 통해 선형결합을 찾는다)

### 1.2.3 Clustering의 적용 사례
Clustering은 생물학, 경형학, 천문학, 고고학, 의/약학, 화학, 교육, 심리학 등 다양한 분야에 적용
- Chemistry
    - 원소 주기율표는 군집 개념을 성공적으로 설명한 예
- Marketing
    - Market Segmentation: 특정 형태의 광고에 더 수용적이거나 특정 제품을 더 잘 구매할 것 같은 사람들의 서브그룹들을 식별하고, 그룹별로 마케팅 전략을 수립
    - Market Structure Analysis: Competitive measures of similarity(경쟁 유사도)에 근거하여 유사제품을 식별
- Finance
    - 다양한 투자 정보 데이터, 재무성과변수, 산업과 시장의 자본총액 같은 특성치를 이용해 그 특징들이 유사한 군집을 형성하고, 서로 다른 군집으로부터 주식을 선택하여 균형 있는 포트폴리오를 구성할 수 있음

### 1.2.4 Clustering Methodologies
- Partitional Clustering(분할적 군집)
    - 군집의 수를 미리 정하고 이에 대해 군집화를 진행하며, __전체 데이터를 서로 겹치지 않게 분할하는 방법__
    - 계층적 군집 방법에 비해 계산량이 적기 때문에 대용량 데이터에서 선호됨(K-means)
- Hierarchical Clustering(계층적 군집)
    - __한 군집 내 부분 군집을 허용하는 방법__ 으로, N개의 데이터를 각각 하나의 군집으로 간주하고 데이터의 특성이 가까운 군집끼리 순차적으로 합해 나가는 병합적 방법(Agglomerative method)이 주로 사용됨
    - 군집들을 자연적인 계층구조로 정렬하고자 할 때 특히 유용(Dendrogram을 이용)

### 1.2.5 군집분석
- 분할적 군집(Partitional Clustering)
    - 프로토타입 기반
      - K-중심 군집
        - __K-평균 군집__
      - 퍼지 군집
        - __FCM 알고리즘 군집__
    - 분포 기반
      - 혼합분포 군집
    - 밀도 기반
      - 중심 밀도 군집
      - 격자 기반 군집
      - 커넬 기반 군집
    - 그래프 기반
      - 코호넨 군집
- 계층적 군집(Hierarchical Clustering)
  - 응집형/상향식
    - 단일(최단) 연결법
    - 완전(최장) 연결법
    - __평균 연결법__
    - 중심 연결법
    - Ward Linkage Method
  - 분리형/하향식
    - 다이아나 방법

### 1.3 Practical Issues

### 1.3.1 Small Decisions with Big Consequences
클러스터링에 앞서 표준화의 필요 여부, 비유사성 측도 및 연결 방법의 선택, 절단점, 군집의 수 등을 사전에 정해야 한다.    

#### 클러스터링에 앞서 결정해야 할 것들
- 우선 데이터를 살피고 __표준화__ 가 필요한지 확인(변수들의 스케일 차이가 큰 경우 거리가 편향되어 계산될 수 있음)
- Hierarchical Clustering의 경우,
  - 어떤 비유사성 측도([Euclidean Distance](#용어정리), Correlation based Distance)를 이용할 것인가?
  - 어떤 연결 방법을 이용할 것인가?(Centroid, Average, Complete ...)
  - 클러스터를 결정하기 위해 어디에서 Dendrogram을 절단할 것인가?
- K-means Clustering의 경우, 몇 개의 클러스터로 나눌 것인가?
- 클러스터링을 통해 얻은 클러스터가 데이터 내의 실제 서브 그룹들을 대표하는지 아니면 단순히 잡음 클러스터링의 결과인지 확인하는 작업이 필요함(확인을 위해 각 클러스터에 p-value를 할당하는 기법이 존재)

### 1.3.2 Other Considerations in Clustering
군집분석 알고리즘은 Outlier에 민감하고, Outlier가 존재하는 경우 결과가 왜곡될 수 있다. 이런 경우 혼합모형을 이용하면 문제를 개선할 수 있다.
#### 클러스터링할 때 고려해야 할 다른 것들
- 각 관측치를 하나의 클러스터에 할당하는 것이 적절하지 않은 경우가 존재할 수 있음
- K-means Clustering은 관측치에 하나의 클러스터만 할당할 수 있으므로 어떤 클러스터에도 속하지 않는 Outlier가 있는 경우 결과가 왜곡될 수 있음
- 이런 경우에는 Misture model(혼합 모델)을 이용하여 문제점을 개선 가능 (혼합모델은 k-means clustering의 soft 버전)
- 또한, 클러스터링 방법들은 일반적으로 데이터의 변동에 대해 robust 하지 않아 가령 n개의 관측치 중 단 하나만 빠져도 군집분석 결과가 바뀔 수 있음

## 2. K-means Clustering
### 2.1 What is K-means Clustering?
K-means Clustering은 [Partitional Clustering](#용어정리)의 하나로 사전 정의된 군집의 수 K를 이용해 각 관측치당 하나의 군집을 할당한다.

### 2.1.1 K-Means Clustering(K-평균 군집)    
- 자료를 사전에 정의된 K개의 서로 다른 겹치지 않는 클러스터로 분할하는 방법    
- 모두 분포는 똑같다.

### 2.1.2 K-Means Clustering의 성질
- 각 관측치는 K개 클러스터 중 적어도 하나에 속한다.
- 클러스터는 겹치지 않는다. 즉, 어떠한 관측치도 두 개 이상의 클러스터에 속하지 않는다.

### 2.1.3 K-Means Clustering의 목적
- K-Means Clustering은 군집 내 변동을 줄이고, 군집 간 변동을 크게 하는 것이 목적
- 클러스터 C_k(K번째 클러스터를 의미)에 대한 클러스터 내 변동은 클러스터 내의 관측치들이 얼마나 서로 다른가를 이용해 나타낼 수 있음. 이때 다른 정도를 나타내는 측도를 W(C_k)라고 하면
- K개 클러스터 모두를 합산한 클러스터 내 총 변동이 가능한 한 작게 되도록 관측치들을 K개 클러스터로 분할하고자 한다는 것을 의미
- 보통 클러스터 내 변동은 [Squared Euclidean Distance(유클리드 거리 제곱)](#용어정리)를 계산하여, 해당 데이터에서 가장 가까운 클러스트를 찾아 데이터를 배당한다.
- 편입된 데이터를 바탕으로 다시 클러스터의 중심을 재조정하며, Continuous iteration을 시행, variable coefficient의 threshold까지 시행 후 종료

### 2.2 How to Find K?
- 초기값에 따른 군집
  - K-means는 전역 최적이 아닌 국소 최적을 찾기 때문에 초기 군집에 따라 결과가 달라진다.
  - 따라서 군집 초기값을 다르게 하여 여러 번 실행한 뒤 각 결과를 비교해 목적함수를 최소화하는 것을 선택하는 것이 좋다.

### 2.3 K-medoids
K-means 모델은 개념이 매우 명확하고 직관적이다.    
또한 학습을 위한 데이터 계산이 매우 빠르다. 하지만 몇 가지 단점이 존재한다.    
- 중심으로부터 거리를 기반으로 군집화하기 때문에 구형으로 뭉쳐져 있는 볼록한 데이터셋에는 비교적 잘 적용되나, 오목한 형태의 군집 모델은 특성을 구별해내는 데 문제를 보인다.
- 또한 동떨어져 있는 데이터(Outlier)나 노이즈에 매우 민감하게 반응하며, 사전에 클러스터 개수를 정하는 것도 단점 중 하나라고 할 수 있다.
- 초기 클러스터를 어떻게 선택하느냐에 따라 글로벌 최솟값에 도달하지 못하는 경우도 있다.

### 2.3.1 Mean vs. Median
K-means(k 최소평균값, 중앙객체값) 알고리즘에서 cluster의 중심에 outlier가 배정되어 있다면, 이 outlier도 평균 계산에 참여하니까 새로운 군집 중심이 왜곡될 것이다.
- 이러한 왜곡을 완화시켜주는 역할을 하는 것이 K-medoids
- 클러스터의 중심을 좌표평면상 임의의 점이 아니라 데이터셋의 값 중 하나를 선정
- 실제 데이터셋에 있는 값을 중심점으로 하기 때문에 Outlier와 노이즈 처리가 우수하고, 매우 robust하게 수렴한다.

## 3 Hierarchical Clustering
### 3.1 What is Hierarchical Clustering?
계층적 군집은 한 군집 내에 부분군집을 허용하는 방법으로 Dendrogram을 이용한 시각화를 활용해 군집의 수를 결정할 수 있는 장점이 존재한다.

### 3.1.1 Hierarchical Clustering (계층적 군집)
- 한 군집 안에 부분 군집을 허용하는 방법
  - 전체 데이터를 하나의 군집으로 하고 이를 부분 군집으로 나눈 후, 각 부분 군집을 다시 나누는 계층형식의 군집방법
- 사전 군집 수 지정이 불필요
  - Dendrogram이라는 tree-based 시각화 방법을 통해 군집을 분석 (시각화 결과를 통한 직관적 군집 수 결정 가능)
- 하향식(Divisive) 또는 상향식(Agglomerative) Clustering
  - 하나의 커다란 군집에서 분리해나가는 방법(하향식/분리형), 또는 각 개체를 응집하며 적정 클러스터를 만드는 방법 (상향식/응집형)이 있음

### 3.1.2 Hierarchical Clustering은 항상 Partitional Clustering보다 좋을까?
- Dendrogram을 이용한 Hierarchical clustering은 partitional clustering인 K-means Clustering보다 많은 정보를 주기 때문에 더 합리적인 군집분석 방법인 것처럼 보인다.
- 하지만 Hierarchical Clustering이 "항상 더 우수한 결과를 가져다 주는 것은 아니다"
- Hierarchical Clustering이 더 안 좋을 수 있는 예
  - 성별(남/여), 인종(황인/백인/흑인)으로 구분된다고 할 때
  - K = 2인 경우, 성별을 기준으로 군집이 나뉘는 것이 합당
  - K = 3인 경우, 인종으로 나뉘는 것이 합당
  - 하지만, Hierarchical Clustering은 성별의 하위그룹으로 인종이 나오거나 인종의 하위그룹으로 성별이 나뉜다는 보장이 없다(이 상황은 Hierarchical Clustering으로 적절히 표현하기 어렵다)

### 3.2 Interpreting a Dendrogram

### 3.2.1 연결의 종류와 방법
- 단일연결 - 사이값이 짧은 것
- 완전연결 - 사이값이 긴 것
- 평균연결 - 존재하지 않는 값을 찾을 수 있다
- 무게중심연결 - 존재하는 값에서 찾는다.