package com.itheima.ATMSystem;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class ATM {
    private ArrayList<Account> accounts = new ArrayList<>();
    private Account loginAcc;
    private Scanner sc = new Scanner(System.in);
    //启动atm系统，展示欢迎页面
    public void start(){
        while (true) {
            System.out.println("===欢迎您进入了ATM系统===");
            System.out.println("1.用户登录");
            System.out.println("2.用户开户");
            System.out.println("请选择：");
            int command = sc.nextInt();
            switch (command){
                case 1:
                    login();
                    break;
                case 2:
                    creatAccount();
                    break;
                default:
                    System.out.println("没有读取到操作");
            }
        }
    }
    //登录功能
    private void login(){
        if(accounts.isEmpty()) return;
        while (true) {
            System.out.println("请输入您的登录卡号：");
            String cardId = sc.next();
            Account acc = getAccountByCardId(cardId);
            if(acc==null){
                System.out.println("您输入的账户不存在");
            }
            else {
                while (true) {
                    System.out.println("请输入您的账户密码：");
                    String passWord = sc.next();
                    if(passWord.equals(acc.getPassWord())){
                        loginAcc = acc;
                        System.out.println("恭喜您"+acc.getUserName()+"已成功登录");
                        //展示后登录后的操作界面
                        showUserCommand();
                        return;
                    }
                    else {
                        System.out.println("您输入的代码有误，请重新确认");
                    }
                }
            }
        }
    }

    //展示登陆后的操作界面
    private void showUserCommand(){
        while (true) {
            System.out.println(loginAcc.getUserName()+"您可以选择如下操作进行账户管理");
            System.out.println("1. 查询账户");
            System.out.println("2. 存款");
            System.out.println("3. 取款");
            System.out.println("4. 转账");
            System.out.println("5. 密码修改");
            System.out.println("6. 退出");
            System.out.println("7. 注销当前账户");
            System.out.println("请选择");
            int command = sc.nextInt();
            switch (command){
                case 1:
                    showLoginAccount();
                    break;
                case 2:
                    depositMoney();
                    break;
                case 3:
                    drawMoney();
                    break;
                case 4:
                    transferMoney();
                    break;
                case 5:
                    updatePassword();
                    return;
                case 6:
                    System.out.println(loginAcc.getUserName()+"您退出系统成功");
                    return;
                case 7:
                    if(deleteAccount()) return;
                    break;
                default:
            }
        }
    }
    //查询账户功能
    private void showLoginAccount(){
        System.out.println("==当前您的账户信息如下==");
        System.out.println("卡号："+loginAcc.getCardId());
        System.out.println("户主："+loginAcc.getUserName());
        System.out.println("性别："+loginAcc.getSex());
        System.out.println("余额："+loginAcc.getMoney());
        System.out.println("每次取现额度："+loginAcc.getLimit());
    }
    //存钱功能
    private void depositMoney(){
        System.out.println("==存款操作==");
        System.out.println("请输入您的存款金额：");
        double money = sc.nextDouble();
        //可以在前端对输入金额进行限制，比如不能输入负数
        loginAcc.setMoney(loginAcc.getMoney()+money);
        System.out.println("恭喜您，存钱"+money+"成功，您的账户余额为"+loginAcc.getMoney());
    }
    //取钱功能
    private void drawMoney(){
        System.out.println("==取钱操作==");
        if(loginAcc.getMoney()<100){
            System.out.println("您的账户余额不足100");
            return;
        }
        while (true) {
            System.out.println("请输入您的取款金额");
            double money = sc.nextDouble();
            if(money<= loginAcc.getMoney()){
                if(money<=loginAcc.getLimit()){
                    loginAcc.setMoney(loginAcc.getMoney()-money);
                    System.out.println("您取款"+money+"成功，取款后您余额剩余"+loginAcc.getMoney());
                    break;
                }
                else{
                    System.out.println("您当前的取款超过了每次取款的限额");
                }

            }
            else{
                System.out.println("您的余额不足");
            }
        }
    }
    //转账
    private void transferMoney(){
        System.out.println("==用户转账==");
        if (accounts.size()<2){
            System.out.println("当前系统中只有您一个账户，无法转账");
            return;
        }
        if (loginAcc.getMoney()<=0){
            System.out.println("没钱就别转账了");
            return;
        }
        while (true) {
            System.out.println("请输入对方卡号");
            String cardId = sc.next();
            //
            Account acc = getAccountByCardId(cardId);
            if(acc==null){
                System.out.println("您输入的卡号不存在");
            }
            else {
                String name="*"+acc.getUserName().substring(1);
                System.out.println("请您输入【"+name+"】的姓氏：");
                String preName = sc.next();
                if (acc.getUserName().startsWith(preName)){
                    while (true) {
                        System.out.println("请输入您的转账金额");
                        double money = sc.nextDouble();
                        if (money<=loginAcc.getMoney()){
                            loginAcc.setMoney(loginAcc.getMoney()-money);
                            acc.setMoney(acc.getMoney()+money);
                            return;
                        }
                        else {
                            System.out.println("您的余额不足，无法转账，您最多转账"+loginAcc.getMoney());
                        }
                    }
                }
                else {
                    System.out.println("对不起，您的姓氏认证出现了问题~~");
                }
            }
        }
    }
    //销户操作
    private boolean deleteAccount(){
        System.out.println("==销户操作==");
        System.out.println("请问您是否要销户y/n");
        String command = sc.next();
        switch (command){
            case "Y":
            case "y":
                if (loginAcc.getMoney()>0){
                    System.out.println("您的账户余额不为0，无法销户");
                    return false;
                }
                else {
                    System.out.println(loginAcc.getUserName()+"您已销户成功");
                    accounts.remove(loginAcc);
                    return true;
                }
            default:
                System.out.println("好的，您的账户保留");
                return false;
        }

    }
    //修改密码
    private void updatePassword(){
        System.out.println("==修改密码==");
        while (true) {
            System.out.println("请输入您的原密码");
            String oldPassWord = sc.next();
            if(loginAcc.getPassWord().equals(oldPassWord)){
                while (true) {
                    System.out.println("请输入您的新密码");
                    String passWord = sc.next();
                    System.out.println("请确认您的新密码");
                    String okPassWord = sc.next();
                    if(passWord.equals(okPassWord)){
                        System.out.println("修改密码成功"+loginAcc.getUserName());
                        loginAcc.setPassWord(passWord);
                        return;
                    }
                    else {
                        System.out.println("两次密码不一致");
                    }
                }
            }
            else {
                System.out.println("您输入的密码错误~");
            }
        }
    }
    //开户功能
    private void creatAccount(){
        //创建一个账户对象，封装用户开户信息
        Account acc= new Account();
        //
        System.out.println("请输入您的账户名称：");
        String name = sc.next();
        acc.setUserName(name);

        while (true) {
            System.out.println("请输入您的性别");
            char sex = sc.next().charAt(0);
            if(sex=='男'||sex=='女'){
                acc.setSex(sex);
                break;
            }
            else{
                System.out.println("您输入的性别有误~只能是男或者女");
            }
        }

        while (true) {
            System.out.println("请输入您的账户密码");
            String passWord=sc.next();
            System.out.println("请确认您的账户密码");
            String okPassWord=sc.next();
            if(passWord.equals(okPassWord)){
                acc.setPassWord(passWord);
                break;
            }
            else {
                System.out.println("您输入的两次密码不一样~请重新设置密码");
            }
        }

        System.out.println("请输入您的取现额度");
        double limit = sc.nextDouble();
        acc.setLimit(limit);
        //设置卡号
        String cardId = createCardId();
        acc.setCardId(cardId);
        //创建成功
        accounts.add(acc);
        System.out.println("恭喜您，"+acc.getUserName()+"开户成功，您的卡号是:"+acc.getCardId());
    }
    //返回一个八位数字的卡号，且不能重复
    private String createCardId(){
        while (true) {
            String cardID="";
            Random r = new Random();
            for (int i = 0; i < 8; i++) {
                int data = r.nextInt(10);
                cardID+=data;
            }
            Account acc  = getAccountByCardId(cardID);
            if (acc==null){
                return cardID;
            }
        }
    }
    //根据卡号查找账户
    private Account getAccountByCardId(String cardId){
        for (int i = 0; i < accounts.size(); i++) {
            Account acc = accounts.get(i);
            if (cardId.equals(acc.getCardId())){
                return acc;
            }
        }
        return null;
    }

}
