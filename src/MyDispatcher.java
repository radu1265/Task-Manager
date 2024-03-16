/* Implement this class. */

import java.util.List;


public class MyDispatcher extends Dispatcher {
    private volatile int hostID = 0;
    public MyDispatcher(SchedulingAlgorithm algorithm, List<Host> hosts) {
        super(algorithm, hosts);
    }
    private final Object lock = new Object();

    // lock is used to make sure that only one thread can access the hostID variable at a time
    // hostID is used to keep track of which host to assign the task to
    @Override
    public void addTask(Task task) {
        if (algorithm == SchedulingAlgorithm.ROUND_ROBIN) {
            synchronized (lock) {
                hosts.get(hostID).addTask(task);
                hostID = (hostID + 1) % hosts.size();
            }
        } else if (algorithm == SchedulingAlgorithm.SHORTEST_QUEUE) {
            synchronized (lock) {
                int min = Integer.MAX_VALUE;
                int minID = 0;
                for (int i = 0; i < hosts.size(); i++) {
                    if (hosts.get(i).getQueueSize() < min) {
                        min = hosts.get(i).getQueueSize();
                        minID = i;
                    }
                }
                hosts.get(minID).addTask(task);
            }
        } else if (algorithm == SchedulingAlgorithm.SIZE_INTERVAL_TASK_ASSIGNMENT) {
            synchronized (lock) {
                if (task.getType() == TaskType.SHORT) {
                    hosts.get(0).addTask(task);
                } else if (task.getType() == TaskType.MEDIUM) {
                    hosts.get(1).addTask(task);
                } else if (task.getType() == TaskType.LONG) {
                    hosts.get(2).addTask(task);
                }
            }
        } else if (algorithm == SchedulingAlgorithm.LEAST_WORK_LEFT) {
            synchronized (lock) {
                int min = Integer.MAX_VALUE;
                int minID = 0;
                for (int i = 0; i < hosts.size(); i++) {
                    if (hosts.get(i).getWorkLeft() < min) {
                        min = (int) hosts.get(i).getWorkLeft();
                        minID = i;
                    }
                }
                hosts.get(minID).addTask(task);
            }
        }
    }
}