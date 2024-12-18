package mr.demonid.service.payment.services.steps;

import mr.demonid.service.payment.exceptions.PaymentStepException;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * Оркестратор. Но без отката операций назад.
 *
 */
public class OrchestratorPayment<T> {

    private final List<PaymentStep<T>> steps = new ArrayList<>();


    public void addStep(PaymentStep<T> step) {
        steps.add(step);
    }

    /**
     * Выполнение заданной последовательности и в случае ошибки на каком-то
     * этапе - автоматический откат всех проделанных действий.
     * @param context Контекст данных по операции.
     */
    public void execute(T context) throws PaymentStepException {
        Deque<PaymentStep<T>> executedSteps = new ArrayDeque<>();
        try {
            for (PaymentStep<T> step : steps) {
                step.execute(context);
                executedSteps.push(step);       // помещаем в стек, на случай отката
            }
        } catch (PaymentStepException e) {
            // откатываем операции.
            while (!executedSteps.isEmpty()) {
                executedSteps.pop().rollback(context);
            }
            throw e;            // и выходим через обработчик исключений
        }
    }

}
