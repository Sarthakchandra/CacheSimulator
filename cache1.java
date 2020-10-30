package com.company;
import java.util.*;
public class cache1{
    static int x; static int y;
    static int te;
    
    //30% Assignment
    
    public static void main(String[] args) {
        Scanner in=new Scanner(System.in);
        System.out.println("Enter the Size of Cache (in terms of 2^) ");
        x = in.nextInt();
        System.out.println("Enter the Size of Data Block (in terms of 2^) ");
        y = in.nextInt();
        te = (int) Math.pow(2, x-y);
        System.out.println("The following types of caches are available:");
        System.out.println("1.) Direct Mapped Cache (Enter Set Size as 0)");
        System.out.println("2.) Fully Associative Cache (Enter Set Size as Size of Cache - Size of Data Block chosen)");
        System.out.println("3.) Set Associative Cache");
        System.out.println("Please select set size depending on your choice: ");
        System.out.println("Enter Size of Set");
        int z = in.nextInt();
        cache o = new cache(x,y,z,te);
        int choice;
        do
        {
            System.out.println("What do you want to do");
            System.out.println("1.) Write to address.");
            System.out.println("2.) Read from address.");
            System.out.println("3.) Print complete cache.");
            System.out.println("4.) Exit");
            System.out.println("Please enter your choice: ");
            choice = in.nextInt();
            if (choice == 1){
                o.write(in.next(),in.nextInt());
            }
            else if (choice == 2){
                o.read(in.next());
            }
            else if (choice ==3){
                o.print();
            }
        }
        while (choice != 4);
    }
}
class cache {
    static int[] top;
    static String[] a_tag;
    static int[][] a_data;
    static int s_size;
    static int s_num;
    static int indices;
    static int offset;

    static int c_size;
    static int b_size;
    static int b_num;
    static int tag;
    cache(int a, int b, int c, int d) {
        c_size = a;
        b_size = b;
        s_size = c;
        b_num = d;
        s_num = b_num / s_size;
        a_tag = new String[b_num];
        offset = (int)(Math.log(b_size) / Math.log(2));
        indices = (int) (Math.log(s_num) / Math.log(2));
        tag = 32 - offset - indices;
        top = new int[s_num];
        a_data = new int[b_num][b_size];
    }
    static int index;
    static int offset1;
    static String d_to_b(int d) {
        StringBuilder s = new StringBuilder();
        for (; d > 0; d /= 2) {
            s.insert(0, d % 2);
        }
        while ( s.length() < indices ) {
            s.insert(0, "0");
        }
        return s.toString();
//Elysium wasn't a good experience bro
    }
    static void print() {
        int i = 0;
        while ( i < b_num ) {
            if (a_tag[i] != null) {
                System.out.println(i);
                System.out.println("T: ");
                System.out.println(a_tag[i]);
                System.out.println("I: ");
                System.out.println(d_to_b(i / s_size));
                System.out.println("D: ");

                int j = 0;
                while ( j < b_size ) {
                    if (a_data[i][j] != 0)
                        System.out.println(j + " " + a_data[i][j]);
                    j++;
                }
            }
            i++;
        }
    }
    static int index1;
    static int offset2;
    static int inu;

    static void replace(String tags)
    {
        System.out.println("Replaced: " + a_tag[index1 * s_size] + d_to_b(index1));
        int i = index1 * s_size + 1;
        while ( i < (index1 + 1) * s_size ) {
            if (b_size >= 0) System.arraycopy(a_data[i], 0, a_data[i - 1], 0, b_size);
            a_tag[i - 1] = a_tag[i];
            i++;
        }
        int in = (index1 + 1) * s_size - 1;
        a_tag[in] = tags;
        int j = 0;
        while ( j < b_size ) {
            a_data[in][j] = 0;
            j++;
        }
    }

    static void dont_replace(String tags)
    {
        inu = index1 * s_size + top[index1];
        a_tag[inu] = tags;
        top[index1]++;
    }

    static void read(String address) {
        String tags = address.substring(0, cache.tag);
        index1 = b_to_d(address.substring(cache.tag, cache.tag + indices));
        offset2 = b_to_d(address.substring(32 - cache.offset, 32));
        int hit = 0;
        {
            int i = index1 * s_size;
            while ( i < (index1 + 1) * s_size ) {
                if (a_tag[i] != null && a_tag[i].equals(tags)) {
                    System.out.println("Hit");
                    System.out.println("Value: " + a_data[i][offset2]);
                    hit = 1;
                    break;
                }
                i++;
            }
        }
        if (hit == 0) {
            System.out.println("Miss");
            if (top[index1] == s_size)
            {
                replace(tags);
            } else {
                dont_replace(tags);
            }

        }
        System.out.println("Updated");
    }
    static void write(String address, int data) {
        String tags = address.substring(0, cache.tag);
        if (indices == 0) {
            index = 0;
        } else {
            index = b_to_d(address.substring(cache.tag, cache.tag + indices));
            offset1 = b_to_d(address.substring(32 - cache.offset, 32));
            //you can do string manipulation in a better way bud
        }
        int hit = 0;
        {
            int i = index * s_size;
            while ( i < (index + 1) * s_size ) {
                if (a_tag[i] != null && a_tag[i].equals(tags)) {
                    System.out.println("Hit");
                    a_data[i][offset1] = data;
                    hit = 1;
                    break;
                }
                i++;
            }
        }
        if (hit == 0) {
            System.out.println("Miss");
            if (top[index] == s_size) {
            replace1(tags,data);
            } else {
            dont_replace1(tags,data);
            }
        }
        System.out.println("Updated");
    }
    static void replace1(String tags, int data){
        System.out.println("Replaced: " + a_tag[index * s_size] + " " + d_to_b(index));
        int i = index * s_size + 1;
        while ( i < (index + 1) * s_size ) {
            if (b_size >= 0) System.arraycopy(a_data[i], 0, a_data[i - 1], 0, b_size);
            a_tag[i - 1] = a_tag[i];
            i++;
        }
        int in = (index + 1) * s_size - 1;
        a_data[in][offset1] = data;
        a_tag[in] = tags;

    }
    static void dont_replace1(String tags, int data){
        int in = index * s_size + top[index];
        a_tag[in] = tags;
        a_data[in][offset1] = data;
        top[index]++;

    }
    static int b_to_d(String str) {
        int d = 0;
        int i = 0;
        while ( i < str.length() ) {
            if (str.charAt(i) == '1') {
                d += Math.pow(2, str.length() - i - 1);
            }
            i++;
        }
        return d;
    }

        public static void LRU() {
            int capacity = 4;
            int[] arr = new int[8];
            ArrayList<Integer> s=new ArrayList<>(capacity);
            int count=0;
            for(int i:arr)
                if(!s.contains(i))
                {if(s.size()==capacity)
                    {s.remove(0);
                    s.add(capacity-1,i);
                    }
                    else
                        s.add(count,i);
                    ++count;
                }
                else
                {   s.remove((Object)i);
                    s.add(s.size(),i);
                }
    }

}
