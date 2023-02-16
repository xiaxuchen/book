//给你一个大小为 n x n 的二元矩阵 grid ，其中 1 表示陆地，0 表示水域。
//
// 岛 是由四面相连的 1 形成的一个最大组，即不会与非组内的任何其他 1 相连。grid 中 恰好存在两座岛 。
//
//
//
// 你可以将任意数量的 0 变为 1 ，以使两座岛连接起来，变成 一座岛 。
//
//
//
// 返回必须翻转的 0 的最小数目。
//
//
//
// 示例 1：
//
//
//输入：grid = [[0,1],[1,0]]
//输出：1
//
//
// 示例 2：
//
//
//输入：grid = [[0,1,0],[0,0,0],[0,0,1]]
//输出：2
//
//
// 示例 3：
//
//
//输入：grid = [[1,1,1,1,1],[1,0,0,0,1],[1,0,1,0,1],[1,0,0,0,1],[1,1,1,1,1]]
//输出：1
//
//
//
//
// 提示：
//
//
// n == grid.length == grid[i].length
// 2 <= n <= 100
// grid[i][j] 为 0 或 1
// grid 中恰有两个岛
//
//
// Related Topics 深度优先搜索 广度优先搜索 数组 矩阵 👍 360 👎 0


import java.util.Arrays;

//leetcode submit region begin(Prohibit modification and deletion)
class Solution {

    public int shortestBridge(int[][] grid) {
        // 先找到两个岛屿的横纵开始坐标和结束坐标
        final int n = grid.length;
        int[][] visited = new int[n][n];
        int[] aInfo = islandInfo(visited,grid,n);
        System.out.println(aInfo);
        int[] bInfo = islandInfo(visited,grid,n);
        int xLen = 1;
        int yLen = 1;
        if(aInfo[0] < bInfo[0]) {
            int[] temp = bInfo;
            bInfo = aInfo;
            aInfo = temp;
        }
        if (bInfo[2] < aInfo[0]) {
            xLen = aInfo[0] - bInfo[2];
        }
        if(aInfo[1] < bInfo[1]) {
            int[] temp = bInfo;
            bInfo = aInfo;
            aInfo = temp;
        }
        if (bInfo[3] < aInfo[1]) {
            yLen = aInfo[1] - bInfo[3];
        }
        System.out.println(xLen);
        System.out.println(yLen);
        return xLen + yLen - 1;
    }

    private int[] islandInfo(int[][] visited, int[][] grid,int n) {
        int islandX = 0,islandY = 0,islandLength = 1,islandWidth = 1;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (visited[i][j] == 0 && grid[i][j] == 1) {
                   islandX = i;
                   islandY = j;
                   break;
                }
            }
        }

        for (int i = islandX + 1; i < n; i++) {
            if (grid[i][islandY] == 1){
                islandLength ++;
            } else {
                break;
            }
        }
        if (islandLength > 1) {
            for (int i = islandY + 1; i < islandY + islandLength; i++) {
                if (grid[islandX + 1][i] == 1) {
                    islandWidth += 1;
                } else {
                    break;
                }
            }
        }
        System.out.println("`1111");
        for (int i = 0; i < islandLength; i++) {
            for (int j = 0; j < islandLength; j++) {
                if (grid[islandX + i][islandY + j] == 1) {
                    visited[islandX + i][islandY + j] = 1;
                }
            }
        }
        return new int[]{islandX,islandY,islandX+islandLength-1,islandY+islandLength-1,islandLength,islandWidth};
    }
}
//leetcode submit region end(Prohibit modification and deletion)
