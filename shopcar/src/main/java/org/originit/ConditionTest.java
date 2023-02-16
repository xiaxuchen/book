package org.originit;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ConditionTest {

    ReentrantLock lock = new ReentrantLock();

    Condition needOp = lock.newCondition();


}
