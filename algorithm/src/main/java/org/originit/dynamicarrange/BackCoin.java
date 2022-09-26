package org.originit.dynamicarrange;

import org.originit.util.TimeStatisticUtil;

import java.util.Arrays;
import java.util.Collections;

public class BackCoin {


    public static int coinBack(Integer[] coins, Integer sum) {
        int[] arr = new int[sum+1];
        Arrays.sort(coins, Collections.reverseOrder());
        int min = coins[coins.length - 1];
        // N
        for (int i = 1; i <= sum; i++) {
            if (i < min) {
                arr[i] = -1;
            } else {
                // M
                boolean continueIt = false;
                for (Integer coin : coins) {
                    if (i == coin) {
                        arr[i] = 1;
                        continueIt = true;
                        break;
                    }
                }
                if (continueIt) {
                    continue;
                }
                int tmp = -1;
                // M
                for (Integer coin : coins) {
                    // 当i大于coin才能最后一个找零是该硬币
                    if (i > coin) {
                        // 如果除去这个硬币剩下的额度能够凑出
                        if (arr[i - coin] != -1) {
                            int cur = arr[i - coin] + 1;
                            if (cur < tmp || tmp == -1) {
                                tmp = cur;
                            }
                        }
                    }
                }
                arr[i] = tmp;
            }
        }
        return arr[sum];
    }

    private static Integer[] coins;

    private static Integer[] mem;
    public static int recallBackCoin(Integer[] coins, Integer sum) {
        mem = new Integer[sum + 1];
        Arrays.fill(mem, Integer.MAX_VALUE);
        Arrays.sort(coins,Collections.reverseOrder());
        BackCoin.coins = coins;
        recall(0,sum);
        return min;
    }

    static int min = -1;
    private static int recall(int all, Integer sum) {
//        if (min != -1 && all >= min) {
//            return -1;
//        }
        if (mem[sum] != Integer.MAX_VALUE) {
            return mem[sum];
        }
        int cur = Integer.MAX_VALUE;
        for (Integer coin : coins) {
            if (coin > sum) {
            } else if (coin.equals(sum)) {
                if (min == -1 || min > all+1) {
                    min = all + 1;
                }
                mem[sum] = all + 1;
                return all + 1;
            } else {
                int result = recall(all+1,sum - coin);
                if (result != -1 && cur > result) {
                    cur = result;
                }
            }
        }
        mem[sum] = cur == Integer.MAX_VALUE?-1:cur;
        return mem[sum];
    }

    public static void testAll(Integer[] coins, Integer sum) {
        TimeStatisticUtil.timing(()->{
            System.out.println(coinBack(coins,sum));
        },"coinBack");
        TimeStatisticUtil.timing(()->{
            System.out.println(recallBackCoin(coins,sum));
        },"recallBackCoin");
    }

    public static void main(String[] args) {
        testAll(new Integer[]{1,7,10}, 121544);
    }
}
