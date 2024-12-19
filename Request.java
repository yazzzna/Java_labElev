public class Request {
    private int from; 
    private int to;   

    public Request(int from, int to) {
        this.from = from;
        this.to = to;
    }

    public int getFrom() {
        return from;
    }

    public int getTo() {
        return to;
    }

    @Override
    public String toString() {
        return "Request{from=" + from + ", to=" + to + '}';
    }
}
