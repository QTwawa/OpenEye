# QTwawa的Readme:

这是一个我和七哥共同开发的一个视频app，我主要负责的模块是module_home和module_video，接下来就看一下每一个模块实现的具体功能和用到的技术。

## module_home:



![f9866db962d026b17c2beb311601039f 00_00_00-00_00_15](https://github.com/QTwaiwai/OpenEye/blob/master/QTwawa%E7%9A%84Readme/f9866db962d026b17c2beb311601039f%2000_00_00-00_00_15.gif?raw=true)

开始前会有一个Splash展示，首先的主框架用的是BottomNavigation+fragment实现了对这四个模块的跳转，然后再来讲讲首页的homefragment：

![f9866db962d026b17c2beb311601039f 00_00_14-00_00_21](https://github.com/QTwaiwai/OpenEye/blob/master/QTwawa%E7%9A%84Readme/f9866db962d026b17c2beb311601039f%2000_00_14-00_00_21.gif?raw=true)

homefragment的大框架是Tablayout+Viewpager2+fragment的联动实现的日报和推荐的跳转。

日报的整体框架就是一个自定义的RecyclerView，RecyclerView的第一个item是套的Viewpager2，改写了RecyclerView横向滑动的逻辑，让banner可以滑动，解决滑动冲突(虽然解决的很暴力，不算太好)。

推荐也是一个RecyclerView，使用的瀑布流布局(StaggeredGridLayout)，为了更加美观。网络请求使用的是基础的协程+paging3为了让滑动更加丝滑。

## module_video:

![f9866db962d026b17c2beb311601039f 00_00_21-00_00_52](https://github.com/QTwaiwai/OpenEye/blob/master/QTwawa%E7%9A%84Readme/f9866db962d026b17c2beb311601039f%2000_00_21-00_00_52.gif?raw=true)

module_video分为两个activity，通过点击来跳转到不同的activity。

首先是PhotoGraphActivity，有一个starActivity的单例类跳转的时候会传一个包含所有图片的list，然后使用vp2进行展示，点击图片即可退出。

然后就是VideoActivity，使用的第三方库dkplayer，根据传过来的一系列数据组成上半部分，然后根据传入id来请求相关视频的数据，使用NestedScrollView+RecyclerView实现整体滚动效果，网络请求使用的是Retrofit和Rxjava，点赞收藏效果用的sp储存的。

![77e61240f2b0fea3c1372e64ec3aa775](https://github.com/QTwaiwai/OpenEye/blob/master/QTwawa%E7%9A%84Readme/77e61240f2b0fea3c1372e64ec3aa775.gif?raw=true00)

## 使用的技术栈

- ​	使用了MVVM框架
- ​	简单使用了pagin3和协程
- ​	简单封装了基本的网络请求
- ​	使用了jetpack
- ​	使用了一些简单的动画
- ​	用了简单的嵌套滑动，实现了banner

不足的地方是滑动冲突解决的太暴力了，程序首次加载的时候会有短暂空白(虽然我觉得是请求的图片太多导致的)，还有就是控件的使用不太熟悉和熟练。

## 个人总结和展望

​		起初想要进入红岩来，仅仅是听说红岩的厉害，进入的都是很厉害的大佬，打着跟着大佬的脚步进来的，但在为期一年的培训中，从什么都不懂的互联网小白，现在成了可以写出一个属于自己app的小登。在这一个月的考核中，学到了很多的知识，使用到了许多之前没有使用到过的技术，但也有很多的不足，对于Android的了解还不是很多。在往后的安卓学习中会学习更多的知识，例如自定义View，协程等。最后，在考核期间让我体验到了既欢快又紧张的的氛围，网校伙伴们的相互打趣，你不走我不走的想法，让我记忆深刻	