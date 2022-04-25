import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;

class Solution {
    public static int dest, ans;
    public static int[][] dp;
    public static HashMap<Integer, Integer> trap_idx = new HashMap<>();
    public int solution(int n, int start, int end, int[][] roads, int[] traps) {
        dest = end;
        dp = new int[1<<traps.length][n+1];
        for(int i=0; i<(1<<traps.length); i++)
            for(int j=0; j<=n; j++)
                dp[i][j]=Integer.MAX_VALUE;

        for(int i=0; i<traps.length; i++)
            trap_idx.put(traps[i], i);

        LinkedList<LinkedList<Dist>> to_idx = new LinkedList<>();
        LinkedList<LinkedList<Dist>> from_idx = new LinkedList<>();
        for(int i=0; i<=n; i++){
            to_idx.add(new LinkedList<Dist>());
            from_idx.add(new LinkedList<Dist>());
        }

        for (int[] road : roads) {
            int p = road[0];
            int q = road[1];
            int s = road[2];
            to_idx.get(q).add(new Dist(p, s));
            from_idx.get(p).add(new Dist(q, s));
        }

        PriorityQueue<Pair> q= new PriorityQueue<Pair>();
        q.add(new Pair(start, 0, 0));

        while(!q.isEmpty()){
            Pair p = q.poll();
            int pos = p.pos;
            if(pos==end) {
                ans = p.cost;
                break;
            }
            int cost = p.cost;
            int mask = p.mask;

            if(trap_idx.containsKey(pos))
                mask ^= 1<<(trap_idx.get(pos));

            if(cost>=dp[mask][pos])
                continue;

            dp[mask][pos] = cost;

            for(Dist dest : from_idx.get(pos)){
                int d = dest.pos;
                int c = dest.cost;
                boolean can;

                if(trap_idx.containsKey(pos)){

                    int pmask = (1<<trap_idx.get(pos)) & mask;

                    if(trap_idx.containsKey(d)){
                        int dmask = (1<<trap_idx.get(d)) & mask;
                        can = (pmask == 0  && dmask==0) || (pmask!=0 && dmask!=0);
                    }
                    else{
                        can = pmask==0;
                    }

                }

                else{

                    if(trap_idx.containsKey(d)){
                        int dmask = (1<<trap_idx.get(d)) & mask;
                        can = dmask==0;
                    }
                    else{
                        can = true;
                    }

                }


                if(!can)
                    continue;

                q.add(new Pair(d, cost+c, mask));

            }

            for(Dist dest : to_idx.get(pos)){
                int d = dest.pos;
                int c = dest.cost;
                boolean can;

                if(trap_idx.containsKey(pos)){

                    int pmask = (1<<trap_idx.get(pos)) & mask;

                    if(trap_idx.containsKey(d)){
                        int dmask = (1<<trap_idx.get(d)) & mask;
                        can = (pmask == 0  && dmask!=0) || (pmask!=0 && dmask==0);
                    }
                    else{
                        can = pmask!=0;
                    }

                }

                else{

                    if(trap_idx.containsKey(d)){
                        int dmask = (1<<trap_idx.get(d)) & mask;
                        can = dmask!=0;
                    }
                    else{
                        can = false;
                    }

                }

                if(!can)
                    continue;

                q.add(new Pair(d, cost+c, mask));

            }

        }
        return ans;

    }

    static class Dist{
        int pos, cost;
        public Dist(int a, int b){
            pos = a;
            cost = b;
        }
    }
    static class Pair implements Comparable<Pair>{
        int pos, cost, mask;
        Pair(int a, int b, int mask){
            pos = a;
            cost = b;
            this.mask = mask;
        }

        public int compareTo(Pair pair){
            if(this.cost < pair.cost)
                return -1;
            else return 1;
        }
    }


}
