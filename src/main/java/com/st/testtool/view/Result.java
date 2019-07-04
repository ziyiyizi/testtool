package com.st.testtool.view;

import java.util.List;

public class Result {
	//csv中的输入、预期输出，实际输出以及最后测试的结果
    public Object[] inputs;
    public Object[] expects;
    public Object[] outputs;
    public Object[] results;
    public int passed;//通过个数
    public int failed;//失败个数

    public Result(List<String> inputs, List<Object> outputs, List<String> expects, List<Boolean> results) {
        this.inputs = inputs.toArray();
        this.expects = expects.toArray();
        this.outputs = outputs.toArray();
        this.results = results.toArray();
        passed = 0;
        failed = 0;
        for (int i = 0; i < this.results.length; ++i) {
            if (this.results[i].toString().equals("true")) {
                ++passed;
            } else {
                ++failed;
            }
        }
    }

}
