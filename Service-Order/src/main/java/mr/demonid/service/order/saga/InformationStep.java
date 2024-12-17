package mr.demonid.service.order.saga;

import mr.demonid.service.order.exceptions.SagaStepException;

/**
 * Шаг: Информирование пользователя о статусе сделки.
 */
public class InformationStep implements SagaStep<SagaContext> {


    @Override
    public void execute(SagaContext context) throws SagaStepException {
        System.out.println("Information step executed");
    }

    @Override
    public void rollback(SagaContext context) {

    }
}
