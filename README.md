# Visual K Means Clustering Algorithm 可视化K均值聚类算法
### 本程序为本人大一Java课程的期末项目作业，仅供学习与交流

点击demo文件夹下的**run.bat**文件，即可运行程序。

其中，程序主视图如下：

![程序初始状态示意图0](images\程序初始状态示意图0.png)

左侧为绘图区域，右侧为控制区域，而底部为状态区域。

具体而言，控制区域主要包括:

1. n，k输入区域，分别用于控制总的点数以及聚类中心数。
2. GPSM：**G**enerate**P**oint**S**et**M**od，用于控制产生点集的模式。具体包括**Random**和**Input Area**两种方式。其中，Random为随机产生点集；Input Area则是读取控制区域**Input Area**中的数据（仅包含点集数据）。
3. FCCCM：**F**irst**C**luster**C**enter**C**hosen**M**od，用于控制第一次选择聚类中心的模式。具体包括**Random**和**K-Means++**这两种方式。其中，Random为随机产生聚类中心；K-Means++则通过尽量选择总体相距较远的点作为聚类中心。
4. Regenerate按钮：重新产生点集和聚类中心，用于初始化新的点集合聚类中心。
5. clusterMod：聚类模式。具体包括**Once**和**Multiple**。其中，Once表示聚类一次，Multiple表示聚类直到再也无法聚类（质心没有发生改变）。
6. cluster按钮：根据相关的模式进行聚类。
7. Input Area：用于输入点集的区域。

具体效果如下：

![程序聚类效果示意图0](images\程序聚类效果示意图0.png)
