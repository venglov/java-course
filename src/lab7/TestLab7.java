package lab7;

import java.io.IOException;
import java.util.Comparator;

public class TestLab7 {
    public static void main(String[] args) throws IOException {
        AdminUnitList aul = new AdminUnitList();
        aul.read();
//        aul.list(System.out);
        aul.units.sort(new Comparator<AdminUnit>() {
            @Override
            public int compare(AdminUnit o1, AdminUnit o2) {
                return Double.compare(o2.area, o1.area);
            }
        });
//        aul.list(System.out);
//        aul.selectByName(".*mal.*", true).list(System.out);
        aul.getNeighbours(aul.units.get(10), 25).units.forEach(au->{
            System.out.println(au.toString());
        });
    }
}
