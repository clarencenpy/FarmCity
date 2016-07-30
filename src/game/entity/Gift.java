package game.entity;

import java.util.*;

/**
 * A Gift represents a gift with a receiver Player and a sendDate
 */
public class Gift {
    private Player receiver;
    private Date sentDate;

    /**
     * Creates a Gift object with the specified fields
     * @param receiver the gift's receiver
     * @param sendDate the gift's sendDate
     */
    public Gift(Player receiver, Date sendDate) {
        this.receiver = receiver;
        this.sentDate = sendDate;
    }

    /**
     * Creates a Gift object with the specified receiver and crop, sentDate defaults to current date and claimed defaults to false
     * @param receiver the gift's receiver
     */
    public Gift(Player receiver) {
        this(receiver, new Date());
    }

    /**
     * Gets the receiver of the gift
     * @return the receiver of the gift
     */
    public Player getReceiver() {
        return receiver;
    }

    /**
     * Gets the send date of the gift
     * @return the sendDate of the gift
     */
    public Date getSentDate() {
        return sentDate;
    }

}
