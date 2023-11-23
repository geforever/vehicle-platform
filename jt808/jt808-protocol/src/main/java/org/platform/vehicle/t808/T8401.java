package org.platform.vehicle.t808;

import io.github.yezhihao.protostar.annotation.Field;
import io.github.yezhihao.protostar.annotation.Message;
import java.util.ArrayList;
import java.util.List;
import org.platform.vehicle.basics.JTMessage;
import org.platform.vehicle.commons.JT808;

/**
 * @author yezhihao
 * https://gitee.com/yezhihao/jt808-server
 */
@Message(JT808.设置电话本)
public class T8401 extends JTMessage {

    /** @see org.yzh.protocol.commons.Action */
    @Field(length = 1, desc = "类型")
    private int type;
    @Field(totalUnit = 1, desc = "联系人项")
    private List<Contact> contacts;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }

    public void addContact(Contact contact) {
        if (contacts == null)
            contacts = new ArrayList<>(2);
        contacts.add(contact);
    }

    public static class Contact {
        @Field(length = 1, desc = "标志")
        private int sign;
        @Field(lengthUnit = 1, desc = "电话号码")
        private String phone;
        @Field(lengthUnit = 1, desc = "联系人")
        private String name;

        public Contact() {
        }

        public Contact(int sign, String phone, String name) {
            this.sign = sign;
            this.phone = phone;
            this.name = name;
        }

        public int getSign() {
            return sign;
        }

        public void setSign(int sign) {
            this.sign = sign;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder(50);
            sb.append("{sign=").append(sign);
            sb.append(",phone=").append(phone);
            sb.append(",name=").append(name);
            sb.append('}');
            return sb.toString();
        }
    }
}
