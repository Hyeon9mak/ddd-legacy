package calculator.ui;

import calculator.application.CalculatorService;
import calculator.domain.AddNumber;

public class CalculatorController {

    private final InputView inputView;
    private final OutputView outputView;
    private final CalculatorService calculatorService;

    public CalculatorController() {
        this.inputView = new InputView();
        this.outputView = new OutputView();
        this.calculatorService = new CalculatorService();
    }

    public void execute() {
        String inputText = inputView.InputAddNumber();
        AddNumber result = calculatorService.add(inputText);
        outputView.printResult(result.getValue());
    }
}
