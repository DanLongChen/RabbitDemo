package Ag;

import java.util.Scanner;

/**
 * Created by Paser on 2019/2/20.
 */
public class NiuNiuduli {
    public static void main(String[] args) {
        int x=0,f=0,d=0,p=0;
        int num=0;
        Scanner input=new Scanner(System.in);
        x=input.nextInt();
        f=input.nextInt();
        d=input.nextInt();
        p=input.nextInt();
        if(d<x*f){
            num=d/x;
        }else{
            num+=f;
            d-=x*f;
            num+=d/(x+p);
        }
        System.out.println(num);
    }
}
