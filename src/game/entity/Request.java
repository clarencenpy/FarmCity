package game.entity;

/**
 * A Request represents a friend request sent another player
 */
public class Request {
    private Player sender;
    private RequestStatus status;
    private Player receiver;

    /**
     * Creates a Request object with the specified sender and receiver
     * @param sender the sender of this request
     * @param receiver the receiver of this request
     */
    public Request(Player sender, Player receiver) {
        this.sender = sender;
        this.receiver = receiver;
        status = RequestStatus.PENDING;
    }

    /**
     * Creates a Request object with the specified sender, receiver and status
     * @param sender the sender of this request
     * @param receiver the receiver of this request
     * @param status the status of this request
     */
    public Request(Player sender, Player receiver, RequestStatus status) {
        this.sender = sender;
        this.receiver = receiver;
        this.status = status;
    }


    /**
     * Gets the sender of this request
     * @return the sender of this request
     */
    public Player getSender() {
        return sender;
    }

    /**
     * Gets the receiver of this request
     * @return the receiver of this request
     */
    public Player getReceiver() {
        return receiver;
    }

    /**
     * Gets the status of this request
     * @return the status of this request
     */
    public RequestStatus getStatus() {
        return status;
    }

    /**
     * Sets the status of this request
     * @param status the status to be set
     */
    public void setStatus(RequestStatus status) {
        this.status = status;
    }
}
