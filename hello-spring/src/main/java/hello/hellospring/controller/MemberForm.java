package hello.hellospring.controller;

public class MemberForm {
    private String name;//MemberController로 들어와서 name을 MemberForm에 넣은상태 들어온값이 name인걸 보고 name에 저장(setName을 통해서 저장)

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name=name;
    }
}
