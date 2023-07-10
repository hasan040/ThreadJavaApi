class APICallTask implements Runnable {
    private final String callNumber;

    public APICallTask(String callNumber) {
        this.callNumber = callNumber;
    }

    @Override
    public void run() {
        String threadId = Thread.currentThread().getName(); // the ID of the current thread
        System.out.println("Executing API call " +"in thread " + threadId + " ["+callNumber+"]");
    }
}
