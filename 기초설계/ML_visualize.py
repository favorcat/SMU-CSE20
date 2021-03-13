#!/usr/bin/env python
# coding: utf-8

# In[1]:


import pandas as pd
import numpy as np
import matplotlib as mpl
import matplotlib.pyplot as plt
import seaborn as sns
from scipy import stats

# 노트북 안에 그래프를 그리기 위해
get_ipython().run_line_magic('matplotlib', 'inline')

# 그래프에서 격자로 숫자 범위가 눈에 잘 띄도록 ggplot 스타일을 사용
plt.style.use('ggplot')

# 그래프에서 마이너스 폰트 깨지는 문제에 대한 대처
mpl.rcParams['axes.unicode_minus'] = False

train = pd.read_csv('train.csv')


# In[2]:


# 1. 데이터 플레임을 불러와서 데이터 건수와 컬럼수를 표시
train.shape


# In[3]:


# 2. 드레인 데이터의 각 항목에 대한 통계 및 데이터 유형을 조회
train.info()


# In[4]:


# 3-1. 트레인 데이터의 상위 5개 데이터를 불러와서 각 데이터의 세부 내용을 조회
train.head(5)


# In[5]:


# 3-2. 트레인 데이터의 상위 20개 데이터를 불러와서 각 데이터의 세부 내용을 조회
train.head(20)


# In[6]:


# 4. 기온 데이터를 describe로 조회
train['weather'].describe()


# In[7]:


# 5. 트레인 데이터에 null인 데이터가 있는지 조회
train.isnull().sum()


# In[8]:


# 6. 트레인 데이터의 datetime을 시각화해서 보기 편하게 년, 월, 일, 시간, 분, 초로 분해하여 트레인 데이터프레임에 다시 저장
from datetime import datetime
train = pd.read_csv('train.csv',  parse_dates=["datetime"])

train["year"] = train["datetime"].dt.year
train["month"] = train["datetime"].dt.month
train["day"] = train["datetime"].dt.day
train["hour"] = train["datetime"].dt.hour
train["minute"] = train["datetime"].dt.minute
train["second"] = train["datetime"].dt.second

train.shape


# In[9]:


# 1 시간별 (년, 월, 일, 시간, 분, 초)에 따른 대여량을 시각화 해보자(x 시간값, y축 대여량) 

plt.rc('font', family='NanumMyeongjo') # 한글이 깨지는 현상을 막기 위해 폰트는 나눔명조로 변경

figure, ((ax1,ax2,ax3), (ax4,ax5,ax6)) = plt.subplots(nrows=2, ncols=3)
figure.set_size_inches(18,8)

sns.barplot(data=train, x="year", y="count", ax=ax1)
sns.barplot(data=train, x="month", y="count", ax=ax2)
sns.barplot(data=train, x="day", y="count", ax=ax3)
sns.barplot(data=train, x="hour", y="count", ax=ax4)
sns.barplot(data=train, x="minute", y="count", ax=ax5)
sns.barplot(data=train, x="second", y="count", ax=ax6)

ax1.set(ylabel='Count',title="연도별 대여량")
ax2.set(xlabel='month',title="월별 대여량")
ax3.set(xlabel='day', title="일별 대여량")
ax4.set(xlabel='hour', title="시간별 대여량")


# In[10]:


# 2. 전체 대여량, 계절별 대여량, 시간별 대여량, 근무일에 따른 대여량을 시각화하라

fig, axes = plt.subplots(nrows=2,ncols=2)
fig.set_size_inches(12, 10)
sns.boxplot(data=train,y="count",orient="v",ax=axes[0][0])
sns.boxplot(data=train,y="count",x="season",orient="v",ax=axes[0][1])
sns.boxplot(data=train,y="count",x="hour",orient="v",ax=axes[1][0])
sns.boxplot(data=train,y="count",x="workingday",orient="v",ax=axes[1][1])

axes[0][0].set(ylabel='Count',title="대여량")
axes[0][1].set(xlabel='Season', ylabel='Count',title="계절별 대여량")
axes[1][0].set(xlabel='Hour Of The Day', ylabel='Count',title="시간별 대여량")
axes[1][1].set(xlabel='Working Day', ylabel='Count',title="근무일 여부에 따른 대여량")


# In[11]:


# 3. 트레인 데이터의 dayofweek 시각화해서 보기 편하게 년, 월,일, 시간, 분, 초로 분해하여 트레인 데이터 프레임에 다시 담아 조회한다.

train['dayofweek'] = pd.to_datetime(train['datetime']).dt.dayofweek
train.shape


# In[12]:


# 4. 요일별 시간대별 대여량을 시각화
fig,(ax1,ax2,ax3)= plt.subplots(nrows=3)
fig.set_size_inches(18,15)

sns.pointplot(data=train, x="hour", y="count", ax=ax1)

sns.pointplot(data=train, x="hour", y="count", hue="workingday", ax=ax2)

sns.pointplot(data=train, x="hour", y="count", hue="dayofweek", ax=ax3)


# In[13]:


#5. 날씨별, 시간별로 시각화
fig,(ax4)= plt.subplots(nrows=1)
fig.set_size_inches(18,5)

sns.pointplot(data=train, x="hour", y="count", hue="weather", ax=ax4)


# In[14]:


# 6. 계절별, 시간별로 시각화
fig,(ax5)= plt.subplots(nrows=1)
fig.set_size_inches(18,5)

sns.pointplot(data=train, x="hour", y="count", hue="season", ax=ax5)


# In[15]:


#  7. 온도와 등록 사용자 여부, 습도, 풍속 데이터가 어떤 연관관계가 있는지 히트맵 시각화
corrMatt = train[["temp", "atemp", "casual", "registered", "humidity", "windspeed", "count"]]
corrMatt = corrMatt.corr()
print(corrMatt)

mask = np.array(corrMatt)
mask[np.tril_indices_from(mask)] = False

fig, ax = plt.subplots()
fig.set_size_inches(20,10)
sns.heatmap(corrMatt, mask=mask,vmax=.8, square=True,annot=True)


# In[16]:


# 8. 온도, 풍속, 습도에 대한 산점도 시각화 (regplot)

fig,(ax1,ax2,ax3) = plt.subplots(ncols=3)
fig.set_size_inches(12, 5)
sns.regplot(x="temp", y="count", data=train,ax=ax1)
sns.regplot(x="windspeed", y="count", data=train,ax=ax2)
sns.regplot(x="humidity", y="count", data=train,ax=ax3)


# In[17]:


def concatenate_year_month(datetime):
    return "{0}-{1}".format(datetime.year, datetime.month)

train["year_month"] = train["datetime"].apply(concatenate_year_month)

print(train.shape)
train[["datetime", "year_month"]].head()


# In[18]:


#  9. 년과 월 데이터를 붙여서 조회 (barplot) (2011~ 2012)

fig, (ax1, ax2) = plt.subplots(nrows=1, ncols=2)
fig.set_size_inches(18, 4)

sns.barplot(data=train, x="year", y="count", ax=ax1)
sns.barplot(data=train, x="month", y="count", ax=ax2)

fig, ax3 = plt.subplots(nrows=1, ncols=1)
fig.set_size_inches(18, 4)

sns.barplot(data=train, x="year_month", y="count", ax=ax3)

