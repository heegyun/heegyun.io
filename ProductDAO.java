package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.Vector;

public class ProductDAO {

    Connection conn = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    String url = "jdbc:mysql://localhost/javastudy";
    String id = "root";
    String pwd = "1234";

    String sql = null;
    Vector<String> items = null;

    // DB 연결 메소드
    public void connectDB()  {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url,id,pwd);
            System.out.println("데이터베이스 연결 성공");

        }catch (Exception e){
            System.out.println("데이터베이스 연결 실패");
        }
    }

    // DB 연결 종료 메소드
    public void closeDB()  {
        try{
            rs.close();
            pst.close();
            conn.close();
            System.out.println("데이터베이스 연결 해제 완료");
        }catch (Exception e){

        }
    }

    // 콤보박스에 있는 아이템 목록 가져오기
    public Vector<String> getItems(){
        return items;
    }



    // 새로운 상품(데이터) 저장(입력) 메소드
    public boolean newProduct(Product product)  {

            // DB 연결 하기
            connectDB();
            try {
            sql = "INSERT INTO product (prname, price, manufacture) VALUES (?,?,?)";

            String prname = product.getPrname();
            int price = product.getPrice();
            String manufacture = product.getManufacture();

            pst = conn.prepareStatement(sql);
            pst.setString(1, prname);
            pst.setInt(2, price);
            pst.setString(3, manufacture);

            pst.executeUpdate();

        } catch (Exception e) {
                return false;
        }finally {

            closeDB();
        }
        return true;

    }//newProduct()-----------------------

    // 전체 상품 목록을 가져오는 메소드
    public ArrayList<Product> getAll(){
        connectDB();

        sql = "SELECT * FROM product" ;

        ArrayList<Product>  datas = new ArrayList<Product>();

        items = new Vector<String>();
        items.add("전체");

        try{
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();

            while (rs.next()){
                Product p = new Product();
                p.setPrcode(rs.getInt("prcode"));
                p.setPrname(rs.getString("prname"));
                p.setPrice(rs.getInt("price"));
                p.setManufacture(rs.getString("manufacture"));

                datas.add(p);
                items.add(String.valueOf(rs.getInt("prcode")));
            }
        } catch (Exception e) {
            System.out.println("조회 실패");
            return  null;
        }finally {
            closeDB();
        }

        return datas;

    }// getAll()----------------

    // 선택한 상품코드에 해당하는 상품 정보를 가져오는 메소드
    public Product getProduct(int prcode){
        connectDB();
        Product p = new Product();
        try{
            sql = "SELECT * FROM product WHERE prcode = ?" ;


            pst = conn.prepareStatement(sql);
            pst.setInt(1, prcode);

            rs = pst.executeQuery();

            if(rs.next()){
                p.setPrcode(rs.getInt("prcode"));
                p.setPrname(rs.getString("prname"));
                p.setPrice(rs.getInt("price"));
                p.setManufacture(rs.getString("manufacture"));
            }
        } catch (Exception e){
            return  null;
        } finally {
            closeDB();
        }
        return  p;
    }


    // 수정한 상품 정보를 데이타베이스에 다시 저장 하는 메소드
    public boolean  updateProduct(Product product){
        connectDB();

        try {
            sql = "UPDATE product SET prname=?, price=?, manufacture=? WHERE prcode = ?" ;

            pst = conn.prepareStatement(sql);

            pst.setString(1, product.getPrname());
            pst.setInt(2, product.getPrice());
            pst.setString(3, product.getManufacture());
            pst.setInt(4, product.getPrcode());

            pst.executeUpdate();

        } catch (Exception e){
            System.out.println("수정 실패");
            return  false;
        } finally {
            closeDB();
        }
        System.out.println("수정 성공");
        return  true;
    }


    // 선택한 상품코드에 해당하는 상품 정보 삭제 하는 메소드
    public boolean delProduct(int prcode){
        connectDB();

        try{
            sql = "DELETE FROM product WHERE prcode = ?" ;

            pst = conn.prepareStatement(sql);
            pst.setInt(1, prcode);

            pst.executeUpdate();

        } catch (Exception e){
            System.out.println("삭제 실패");
            return false;
        } finally {
            closeDB();
        }
        System.out.println("삭제 성공");
        return  true;
    }
}
