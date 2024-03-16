import java.util.PriorityQueue;

public class MyHost extends Host {
    private PriorityQueue<Task> queue = new PriorityQueue<>(new CompareByPriority());
    private volatile Task currentTask = null;
    private volatile boolean isRunning = true;
    private boolean inWork = false;
    private long workLeft = 0;
    private final long timer = 1000L;

    /**
     * creates a priority queue to store the tasks
     * currentTask keep track of the current task
     * isRunning keep track of whether the host is running
     * inWork keep track of whether the host is working
     * timer is the time interval for the host to check the current task
     * workLeft is the total work left for the host
     * this is used in the LEAST_WORK_LEFT algorithm
     */

    public void run() {
        while (isRunning) {
            if (currentTask == null) {
                if (queue.isEmpty()) {
                    continue;
                    // if the queue is empty, the host will keep checking the queue
                } else {
                    currentTask = queue.poll();
                    inWork = true;
                    // the host will start working on the task with the highest priority
                }
            } else {
                // the host will keep working on the current task
                try {
                    sleep(timer);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                currentTask.setLeft(currentTask.getLeft() - timer);
                workLeft -= timer;
                // update the work left for the host
                if (currentTask.isPreemptible()) {
                    // check if the task is preemptible
                    if (!queue.isEmpty()) {
                        Task nextTask = queue.peek();
                        if (nextTask.getPriority() > currentTask.getPriority()) {
                            queue.add(currentTask);
                            currentTask = queue.poll();
                            // if the next task has a higher priority, the current task will be preempted
                        }
                    }
                }
                if (currentTask.getLeft() <= 0) {
                    // finish the current task
                    currentTask.finish();
                    currentTask = null;
                    inWork = false;
                    // reset variables
                }
            }
        }
    }

    @Override
    public void addTask(Task task) {
        queue.add(task);
        // add the task to the queue
        // also update the work left for the host
        workLeft += task.getLeft();
    }

    @Override
    public int getQueueSize() {
        if (inWork) {
            return queue.size() + 1;
        }
        return queue.size();
    }

    @Override
    public long getWorkLeft() {
        return workLeft;
    }

    @Override
    public void shutdown() {
        isRunning = false;
    }
}

/** class to compare the tasks by their priority
 * higher priority tasks will be placed in front of the queue
 * if two tasks have the same priority,
 * the one with the earlier start time will be placed in front
 */
class CompareByPriority implements java.util.Comparator<Task> {
    @Override
    public int compare(Task t1, Task t2) {
        if (t1 == null) {
            return 1;
        }
        if (t2 == null) {
            return -1;
        }
        if (t1.getPriority() > t2.getPriority()) {
            return -1;
        } else if (t1.getPriority() < t2.getPriority()) {
            return 1;
        } else {
            return t1.getStart() - t2.getStart();
        }
    }
}