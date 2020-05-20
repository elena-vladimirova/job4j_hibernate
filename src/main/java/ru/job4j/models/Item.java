package ru.job4j.models;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "items")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String dsc;
    @ManyToOne
    @JoinColumn(name = "author_id", foreignKey = @ForeignKey(name = "AUTHOR_ID_FK") )
    private User author;
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "item_id", foreignKey = @ForeignKey(name = "ITEMS_FK") )
    private List<Comment> comments;

    public Item() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDesc() {
        return dsc;
    }

    public void setDesc(String desc) {
        this.dsc = desc;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", dsc='" + dsc + '\'' +
                ", author=" + author +
                ", comments=" + comments +
                '}';
    }
}
