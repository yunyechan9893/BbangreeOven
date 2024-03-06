package com.bbangle.bbangle.util;

public class mQueue {

    private int front;
    private int rear;
    private int size;
    private final Object[] queue;

    public mQueue() {
        this.queue = new Object[100]; // Assuming a maximum size of 100, adjust as needed
        this.front = 0;
        this.rear = 0;
        this.size = 0;
    }

    public void enqueue(Object value) {
        this.queue[this.rear++] = value;
        this.size++;
    }

    public Object dequeue() {
        Object value = this.queue[this.front];
        this.queue[this.front++] = null;
        this.size--;
        return value;
    }

    public boolean isEmpty() {
        return this.front == this.rear;
    }

}
