package com.spotbugs.plugin;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.BytecodeScanningDetector;
import edu.umd.cs.findbugs.ba.ClassContext;
import edu.umd.cs.findbugs.ba.XField;
import org.apache.bcel.Const;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class AssignStaticFields extends BytecodeScanningDetector {
  private static final Set<String> IMMUTABLE_REFERENCE =
      new HashSet<>(
          Arrays.asList(
              "java/lang/String",
              "java/io/PrintStream",
              "com/workshi/commonlib/tenant/TenantLocal"));

  private final BugReporter bugReporter;
  LinkedList<XField> seen = new LinkedList<>();

  public AssignStaticFields(BugReporter bugReporter) {
    this.bugReporter = bugReporter;
  }

  @Override
  public void sawClass() {
    ClassContext classContext = getClassContext();
    //    classContext
    System.out.println("sawClass -> " + getClassName());
  }

  @Override
  public void report() {
    System.out.println("Amount of interesting field: " + seen.size());
  }

  @Override
  public void sawField() {
    XField xField = getXFieldOperand();
    System.out.println("Saw Field -> " + xField + " [Name:] " + xField.getName());
    if (interesting(xField)) {
      seen.add(xField);
    }
  }

  @Override
  public void sawInt(int seen) {
    ClassContext classContext = getClassContext();
    //    classContext
    System.out.println("sawInt -> " + seen);
  }

  @Override
  public void sawMethod() {
    System.out.println("sawMethod -> " + getXMethod());
  }

  @Override
  public void sawOpcode(int seen) {
    XField xField2 = getXFieldOperand();
    System.out.println("sawOpcode");
    this.printOpCode(seen);
    switch (seen) {
        //      case Const.GETSTATIC: // Get static field from class
      case Const.PUTSTATIC: // Set static field in class
        XField xField = getXFieldOperand();
        System.out.println("Put Static -> " + xField + " [Name:] " + xField.getName());
        if (xField == null) {
          break;
        }
        if (interesting(xField)) {
          // report bug when System.out is used in code
          BugInstance bug =
              new BugInstance(this, "ASSIGN_MUTABLE_STATIC_FIELD", NORMAL_PRIORITY)
                  .addClassAndMethod(this)
                  .addSourceLine(this, getPC());
          bugReporter.reportBug(bug);
          //          System.out.println("Static object utilize ================== " + xField);
          //          System.out.println("xField.getSignature() -> " + xField.getSignature());
          //          System.out.println("getClassConstantOperand() -> " +
          // getClassConstantOperand());
        }
    }
  }

  private boolean interesting(XField field) {
    if (!field.isStatic()) {
      return false;
    }
    String signature = field.getSignature();
    if (isMutableClasses(signature)) {
      return true;
    }
    return signature.charAt(0) == '['; // reference of one array dimension
  }

  private boolean isMutableClasses(String signature) { // excluding primitive types
    if (signature.startsWith("L") && signature.endsWith(";")) {
      String fieldClass = signature.substring(1, signature.length() - 1);
      return !IMMUTABLE_REFERENCE.contains(fieldClass);
    }
    return false;
  }
}
