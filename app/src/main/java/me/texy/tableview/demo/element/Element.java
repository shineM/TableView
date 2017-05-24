package me.texy.tableview.demo.element;

/**
 * Created by xinyuanzhong on 2017/5/24.
 */
public class Element {
    public String charName;
    public String chineseName;
    public String showIndex;
    public int index;
    public Type type;

    public enum Type {
        NOBLE_GAS("惰性气体"),
        HALOGEN("卤素"),
        NON_METAL("非金属"),
        ALKALI_METAL("碱金属"),
        ALKALI_EARTH_METAL("碱土金属"),
        ACTINIDE_METAL("锕系元素"),
        LANTHANUM_METAL("镧系元素"),
        TRANSITION_METAL("过渡金属"),
        MAIN_GROUP_METAL("主族金属"),
        NONE("");

        private String typeName;

        Type(String typeName) {
            this.typeName = typeName;
        }

        public static Type getEnum(String typeName) {
            for (Type t : Type.values()) {
                if (typeName.equals(t.typeName)) return t;
            }
            return null;
        }
    }
}
