package Ag;

import java.util.Scanner;

/**
 * Created by Paser on 2019/2/20.
 */
public class NiuNiuChiXueGao {
    public static void main(String[] args) {
        int T=0,N=0,A=0,B=0,C=0;
        Scanner input=new Scanner(System.in);
        T=input.nextInt();
        for (int i=0;i<T;i++){
            N=input.nextInt();
            A=input.nextInt();
            B=input.nextInt();
            C=input.nextInt();
            NiuNiuChiXueGao.show(N,A,B,C);
        }
    }
    public static void show(int N,int A,int B,int C){
        int k1=C/2;
        C%=2;
        if(k1>=0){
            N-=k1;
        }
        int k2=B/3;
        B%=3;
        if(k2>=0){
            N-=k2;
        }
        int k3=A/6;
        A%=6;
        if(k3>=0){
            N-=k3;
        }
        if(A>=1 && B>=1 && C==1){
            C=0;
            B--;
            A--;
            N--;
        }
        if(A>=2 && B==2){
            A-=2;
            B=0;
            N--;
        }
        if(A>=3 && C==1){
            A-=3;
            C=0;
            N--;
        }
        if(A>=4 && B==1){
            A-=4;
            B=0;
            N--;
        }

        if(N>0){
            System.out.println("No");
        }else{
            System.out.println("Yes");
        }
    }


}
