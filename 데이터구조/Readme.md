# 데이터구조
> 2021학년도 1학기 수강과목
---
### 목차
1. [데이터구조 개요](#1-데이터구조-개요)
    1. [프로그램과 알고리즘 비교](#11-프로그램과-알고리즘-비교)
    2. [프로그램](#12-프로그램)
    3. [알고리즘](#13-알고리즘)
2. [시간복잡도 평가](#2-시간복잡도-평가)
3. [알고리즘 성능 분석 표기법](#3-알고리즘-성능-분석-표기법)
    1. [빅-오 표기법](#31-빅-오-표기법)
    2. [빅-오메가 표기법](#32-빅-오메가-표기법)
    3. [빅-세타 표기법](#33-빅-세타-표기법)
4. [배열](#4-배열)
    1. [1차원 배열의 주소 계산](#41-1차원-배열의-주소-계산)
    2. [다차원 배열의 메모리 표현](#42-다차원-배열의-메모리-표현)
        1. [column-major(열 우선)](#421-column-major열-우선)
        2. [row-major(행 우선)](#422-row-major행-우선)
---

### 1. 데이터구조 개요

### 1.1 프로그램과 알고리즘 비교
#### 프로그램과 알고리즘의 유사점
어떤 문제를 해결하기 위한 절차

#### 프로그램과 알고리즘 차이점
##### 프로그램(program)
- 문제 해결 절차를 프로그래밍 언어로 기술
- C언어로 기술 -> C 프로그램
- Java로 기술 -> Java 프로그램

##### 알고리즘(algorithm)
- 문제 해결 절차를 기술하는 수단에 제한없음

### 1.2 프로그램
프로그램 = 데이터구조 + 알고리즘

```C
#include <stdio.h>
int main(){
    int su1 = 10, su2 = 20, hap;            // 데이터구조
    
    hap = su1 + su2;                        // 알고리즘
    printf("%d + %d = %d", su1, su2, hap);  // 알고리즘
}
```

#### 좋은 프로그램 조건
- 적은 메모리
- 빠른 처리
- 이해 용이성
- 구조 명확
- 디버깅 용이 / 변경 용이

### 1.3 알고리즘
#### 알고리즘 표현 방법
- 프로그래밍 언어 (ex. C언어 등)
- 자연언어 (ex. 한국어, 영어, 일본어 등))
- 의사코드(pseudo code)
    자연어보다 구조적, 프로그래밍 언어보다 덜 구체적 표현
    알고리즘 기술에 많이 이용
    ```
    if 신용카드 번호 유효
        해당 번호로 주문거래
    else 에러 표시
    end if
    ```
- 도형
    순서도(flow chart)
        문제 풀이 과정을 도형으로 표현
    PAD(Program Analysis Diagram)

#### 알고리즘의 조건
- 알고리즘의 정당성(correctness)
    알고리즘이 문제 P의 입력 데이터에 대한 정답 계산
- 어떤 입력 데이터에 대해서도 틀린 답은 계산하지 않는다.
    답을 출력하면 반드시 그 답은 정확해야한다.
- 어떤 입력 데이터에 대해서도 유한 시간내에 계산을 완료한다.

### 2. 시간복잡도 평가
문제. 다음 데이터에 대해 숫자 찾기. 차례대로 숫자 비교
| 32 | 0 | 25 | 58 | 66 | 4 | 17 | 90 | 51 | 83 |
|:---|:---|:---|:---|:---|:---|:---|:---|:---|:---|

`90`

최선의 경우(best case)
- 수행 시간이 가장 빠름 : 1회 비교
- 의미가 없는 경우가 많음

평균의 경우(average case)
- 수행시간이 평균적인 경우
    1 + 2 + 3 + ... + 10 = 55/10 = 5.5회 비교
- 계산이 어려움

최악의 경우(worst case)
- 수행 시간이 가장 많은 경우 : 10회 비교
- 계산이 용이하여 가장 널리 이용. 응용에 따라 중요한 의미

### 3. 알고리즘 성능 분석 표기법
알고리즘을 비교하기 위한 성능 분석 표기는 시간 복잡도 표기를 의미한다.    
시간 복잡도는 실행 빈도 함수에서 입력 크기 n에 대한 실행 시간의 증가율만 분석하는 점근적 분석이므로,    
실행 빈도 함수의 상수항과 계수는 무시하고 n의 증가에 따라 증가율이 가장 큰 하나의 항에 대해서 차수 표기법으로 시간 복잡도를 표기한다.    

표기법
1. 제일 차수가 큰 항 이외는 삭제
2. 계수 삭제
3. 수식 내에 기술 (ex. ![o(n)](https://user-images.githubusercontent.com/49950126/111866727-db380e00-89b2-11eb-9bb9-56670cc4ec26.png))

### 3.1 빅-오 표기법
![o(f(n))](https://user-images.githubusercontent.com/49950126/111866644-3e757080-89b2-11eb-8880-6dc7dfc0df90.png) 과 같이 표기하며, __Big Oh of ![f(n)](https://user-images.githubusercontent.com/49950126/111866680-7a103a80-89b2-11eb-9c5f-d8db3062532d.png)__ 으로 읽는다.    
실행 빈도 함수에서 구한 시간 복잡도가 ![n^2](https://user-images.githubusercontent.com/49950126/111866729-e428df80-89b2-11eb-969d-a8b9b1101b80.png) 라면 ![o(n^2)](https://user-images.githubusercontent.com/49950126/111866736-ed19b100-89b2-11eb-805c-9fa73a87568a.png) 으로 표기하고 __Big Oh of ![n^2](https://user-images.githubusercontent.com/49950126/111866741-f4d95580-89b2-11eb-8608-f38662c21793.png)__ 으로 읽는다.
> 함수 ![f(n)](https://user-images.githubusercontent.com/49950126/111866680-7a103a80-89b2-11eb-9c5f-d8db3062532d.png) 과 ![g(n)](https://user-images.githubusercontent.com/49950126/111866764-26522100-89b3-11eb-8c7c-494e5be34a6b.png) 이 주어졌을 때, 모든 ![n  ge n_0](https://user-images.githubusercontent.com/49950126/111866812-67e2cc00-89b3-11eb-834f-88d4999fa82b.png) 에 대하여 ![|f(n)|  le c |g(n)|](https://user-images.githubusercontent.com/49950126/111866816-716c3400-89b3-11eb-8a9c-58cb91bdfb8d.png) 을 만족하는 상수 ![c](https://user-images.githubusercontent.com/49950126/111866820-7e892300-89b3-11eb-8b6a-a22c2d3b5633.png) 와 ![n_0](https://user-images.githubusercontent.com/49950126/111866829-919bf300-89b3-11eb-9160-cc35096d2019.png) 이 존재하면, ![f(n) = O(g(n))](https://user-images.githubusercontent.com/49950126/111866831-99f42e00-89b3-11eb-9bcf-690841184b53.png) 이다.
    
빅-오 표기법은 __함수의 상한을 나타내기 위한 표기법__    
최악의 경우에도 ![g(n)](https://user-images.githubusercontent.com/49950126/111866764-26522100-89b3-11eb-8c7c-494e5be34a6b.png) 의 수행 시간 안에는 알고리즘 수행이 완료된다는 것을 보장한다.    
빅-오 표기법으로 표현하는 방법은 먼저 실행 빈도수를 구하여 실행 시간 함수를 찾고, 이 함수값에 가장 큰 영향을 주는 n에 대한 항을 한개 선택하여 계수는 생략하고 O의 오른쪽 괄호 안에 표시한다.    

### 3.2 빅-오메가 표기법
![Omega (f(n))](https://user-images.githubusercontent.com/49950126/111866845-a8424a00-89b3-11eb-9249-fd1892f05467.png) 과 같이 표기하며, __Big Omega of ![f(n)](https://user-images.githubusercontent.com/49950126/111866680-7a103a80-89b2-11eb-9c5f-d8db3062532d.png)__ 으로 읽는다.
실행 빈도 함수에서 구한 시간 복잡도가 ![n^2](https://user-images.githubusercontent.com/49950126/111866729-e428df80-89b2-11eb-969d-a8b9b1101b80.png) 라면 ![Omega (f(n^2))](https://user-images.githubusercontent.com/49950126/111866851-b3957580-89b3-11eb-9c97-7b50dd85158d.png) 으로 표기하고 __Big Omega of ![n^2](https://user-images.githubusercontent.com/49950126/111866729-e428df80-89b2-11eb-969d-a8b9b1101b80.png)__ 으로 읽는다.    
> 함수 ![f(n)](https://user-images.githubusercontent.com/49950126/111866680-7a103a80-89b2-11eb-9c5f-d8db3062532d.png) 과 ![g(n)](https://user-images.githubusercontent.com/49950126/111866764-26522100-89b3-11eb-8c7c-494e5be34a6b.png) 이 주어졌을 때, 모든 ![n  ge n_0](https://user-images.githubusercontent.com/49950126/111866812-67e2cc00-89b3-11eb-834f-88d4999fa82b.png) 에 대하여 ![|f(n)|  ge c |g(n)|](https://user-images.githubusercontent.com/49950126/111866868-c8720900-89b3-11eb-8ff4-1f02f968ab7d.png) 을 만족하는 상수 ![c](https://user-images.githubusercontent.com/49950126/111866820-7e892300-89b3-11eb-8b6a-a22c2d3b5633.png) 와 ![n_0](https://user-images.githubusercontent.com/49950126/111866829-919bf300-89b3-11eb-9160-cc35096d2019.png) 이 존재하면, ![f(n) =  Omega(g(n))](https://user-images.githubusercontent.com/49950126/111866875-daec4280-89b3-11eb-8d8b-ef47665a6026.png) 이다.

빅-오메가 표기법은 __함수의 하한을 나타내기 위한 표기법__    
어떤 알고리즘의 시간 복잡도가 ![Omega(g(n))](https://user-images.githubusercontent.com/49950126/111866880-e7709b00-89b3-11eb-9220-0f233b9182dd.png) 으로 분석되었다면, 이 알고리즘 수행에는 적어도 ![g(n)](https://user-images.githubusercontent.com/49950126/111866764-26522100-89b3-11eb-8c7c-494e5be34a6b.png) 의 수행 시간이 필요함을 의미한다.

### 3.3 빅-세타 표기법
![theta(f(n))](https://user-images.githubusercontent.com/49950126/111866892-f48d8a00-89b3-11eb-9d79-3663c7603c15.png) 과 같이 표기하며, __Big Theta of ![f(n)](https://user-images.githubusercontent.com/49950126/111866680-7a103a80-89b2-11eb-9c5f-d8db3062532d.png)__ 으로 읽는다.    
실행 빈도 함수에서 구한 시간 복잡도가 ![n^2](https://user-images.githubusercontent.com/49950126/111866729-e428df80-89b2-11eb-969d-a8b9b1101b80.png) 라면 ![Theta (f(n^2))](https://user-images.githubusercontent.com/49950126/111866902-04a56980-89b4-11eb-974e-14080714c47c.png) 으로 표기하고 __Big Theta of ![n^2](https://user-images.githubusercontent.com/49950126/111866729-e428df80-89b2-11eb-969d-a8b9b1101b80.png)__ 으로 읽는다.    
> 함수 ![f(n)](https://user-images.githubusercontent.com/49950126/111866680-7a103a80-89b2-11eb-9c5f-d8db3062532d.png) 과 ![g(n)](https://user-images.githubusercontent.com/49950126/111866764-26522100-89b3-11eb-8c7c-494e5be34a6b.png) 이 주어졌을 때, 모든 ![n  ge n_0](https://user-images.githubusercontent.com/49950126/111866812-67e2cc00-89b3-11eb-834f-88d4999fa82b.png) 에 대하여 ![c_1 |g(n)|  le f(n)  le c_2 |g(n)|](https://user-images.githubusercontent.com/49950126/111866919-21da3800-89b4-11eb-8f1e-e4a407b8e9dc.png) 을 만족하는 상수 ![c_1](https://user-images.githubusercontent.com/49950126/111866928-29014600-89b4-11eb-8ad1-06e19836d4d0.png),![c_2](https://user-images.githubusercontent.com/49950126/111866933-30285400-89b4-11eb-8ba4-de0b072e6695.png) 와 ![n_0](https://user-images.githubusercontent.com/49950126/111866829-919bf300-89b3-11eb-9160-cc35096d2019.png) 이 존재하면, ![f(n) =  theta(g(n))](https://user-images.githubusercontent.com/49950126/111866954-40403380-89b4-11eb-8053-cbf61c4b87f0.png) 이다.

빅-세타 표기법은 __상한과 하한이 같은 정확한 차수를 표현하기 위한 표기법__    
![f(n) =  theta(g(n))](https://user-images.githubusercontent.com/49950126/111866954-40403380-89b4-11eb-8053-cbf61c4b87f0.png) 이 되려면 ![f(n) = O(g(n))](https://user-images.githubusercontent.com/49950126/111866831-99f42e00-89b3-11eb-9bcf-690841184b53.png) 이면서 ![f(n) =  Omega(g(n))](https://user-images.githubusercontent.com/49950126/111866875-daec4280-89b3-11eb-8d8b-ef47665a6026.png) 이어야 한다.    

시간 복잡도를 정확히 계산할 수 있다면 빅-세타 표기법을 사용한다.    
시간 복잡도를 정확히 분석하기 어렵다면 상한을 구하여 빅-오 표기법으로 사용하거나, 하한을 구하여 빅-오메가 표기법을 사용한다.    
일반적으로 최악의 경우를 고려한 해결책을 찾기 때문에 빅-오 표기법을 주로 사용한다.    
    
알고리즘에 따라 ![log n, n, nlogn, n^2, n^3, 2^n](https://user-images.githubusercontent.com/49950126/111866693-a035da80-89b2-11eb-9aae-61e1bf02ea91.png) 등의 실행 시간 함수가 있다.    
n값이 커질수록 실행 시간 함수값의 크기는 ![logn   n   nlogn   n^2   n^3   2^n](https://user-images.githubusercontent.com/49950126/111866706-b2177d80-89b2-11eb-865a-10fe92d57254.png) 순서로 커진다.    
실행 빈도수가 상수값이라면 ![O(1)](https://user-images.githubusercontent.com/49950126/111866713-c6f41100-89b2-11eb-9db1-a61285aabd70.png) 로 표기하며 가장 빠른 실행 시간 함수가 된다.

### 4. 배열
### 4.1 1차원 배열의 주소 계산
1차원 배열 선언[100]
`int A[100]`
A(i)의 주소 계산
```
address(A(i)) = 시작주소(address(A(0))) + k * i
```

### 4.2 다차원 배열의 메모리 표현
| 1 | 2 | 3 | 4 | ... | n |
|---|---|---|---|---|---|
| 2 |
| ... |
| m |

#### 4.2.1 column-major(열 우선)
열(column)을 우선시 해서 저장
| 1 | 2 | 3 | 4 | ... | n |
|---|---|---|---|---|---|
| 2 |
| 3 | | | [i][j]
| ... |
| m |
```
address(b(i,j)) = address(b(0,0)) + m * j * k + i * k
k = 해당 메모리의 기본 크기 (ex. int = 4)
```

#### 4.2.2 row-major(행 우선)
행(row)을 우선시 해서 저장
__코볼, PL/I, 파스칼, C언어__
| 1 | 2 | 3 | 4 | ... | n |
|---|---|---|---|---|---|
| 2 |
| 3 | | | [i][j]
| ... |
| m |
```
address(b(i,j)) = address(b(0,0)) + i * n * k + j * k
k = 해당 메모리의 기본 크기 (ex. int = 4)
```

##### 예시 1.
C언어에서 다음과 같이 정의한다.    
`int a[5][6];`
a[3][2]의 주소는? (단, a[0][0]의 주소는 1000)
| 1 | 2 | 3 | 4 | 5 | 6 |
|---|---|---|---|---|---|
| 2 |
| 3 | | | 
| 4 | | a[3][2]|
| 5 |

![therefore](https://user-images.githubusercontent.com/49950126/111867003-9f9e4380-89b4-11eb-8c0f-6f251deab12d.png) 1000 + 3 * 6 * 4 + 2 * 4

##### 예시 2.
k[1:2, 1:3, 1:2] 의 배열에서 9번째로 저장된 곳의 좌표는?
| 1 (1,1,1) | 2 (1,1,2) |
|---|---|
| 3 (1,2,1) | 4 (1,2,2)
| 5 (1,3,1)| 6 (1,3,2)| 

| 7 (2,1,1) | 8 (2,1,2) |
|---|---|
| 9 (2,2,1) | 10 (2,2,2) |
| 11 (2,3,1) | 12 (2,3,2) |

![therefore](https://user-images.githubusercontent.com/49950126/111867003-9f9e4380-89b4-11eb-8c0f-6f251deab12d.png) (2,2,1)
