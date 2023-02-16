### Lock
- 通过Lock的Condition可以实现await与interrupt的效果
```
// 线程一、

// 加锁
try {
    while(条件不满足) {
        // 等待
    }
} finally {
    // 解锁
}
// ### 线程二、 
// 条件满足则唤醒

```
### 限流器
当需要限制同时进行的线程数是，可以通过Semaphore信号量机制进行
在操作的时候获取一个名额，完成操作后交还名额
### ReadWriteLock锁的升级与降级
- 不支持升级(r->w),会死锁
- 支持降级(w->r)
- 读锁不支持newCondition
### CyclicBarrier
可以同步多个线程同时在各线程完成后执行特定操作并重新执行源操作
### CAS
#### ABA解决
- Stamp 版本号
- Boolean版本号
### 原子类
可以使用Adder或者Accumulator替代Atomic类来做累加