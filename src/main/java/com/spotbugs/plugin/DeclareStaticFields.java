package com.spotbugs.plugin;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.BytecodeScanningDetector;
import edu.umd.cs.findbugs.ba.XField;
import org.apache.bcel.classfile.Field;
import org.apache.bcel.generic.Type;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class DeclareStaticFields extends BytecodeScanningDetector {
  private static final Set<String> IMMUTABLE_REFERENCE =
      new HashSet<>(
          Arrays.asList(
              "java/lang/String",
              "java/io/PrintStream",
              "com/workshi/commonlib/tenant/TenantLocal"));

  private static final Set<String> IMMUTABLE_TYPE_STRING =
      new HashSet<>(
          Arrays.asList(
              "byte",
              "short",
              "int",
              "long",
              "float",
              "double",
              "char",
              "boolean",
              "java/lang/String",
              "java/io/PrintStream",
              "com/workshi/commonlib/tenant/TenantLocal"));

  private static final Set<Type> IMMUTABLE_TYPE =
      new HashSet<>(
          Arrays.asList(
              Type.BYTE,
              Type.SHORT,
              Type.INT,
              Type.LONG,
              Type.FLOAT,
              Type.DOUBLE,
              Type.CHAR,
              Type.BOOLEAN,
              Type.STRING));

  private final BugReporter bugReporter;
  LinkedList<XField> seen = new LinkedList<>();

  public DeclareStaticFields(BugReporter bugReporter) {
    this.bugReporter = bugReporter;
  }

  @Override
  public void report() {
    System.out.println("Amount of interesting field: " + seen.size());
  }

  @Override
  public void visit(Field obj) {
    System.out.println("visit -> " + obj + ", type: " + obj.getType());
    super.visit(obj);
    if (!IMMUTABLE_TYPE.contains(obj.getType()) && obj.isStatic()) {
      BugInstance bug =
          new BugInstance(this, "DECLARE_MUTABLE_STATIC_FIELD", NORMAL_PRIORITY)
              .addClass(getClassDescriptor());
      bugReporter.reportBug(bug);
    }
  }

  private boolean interesting(Field field) {
    if (!field.isStatic()) {
      return false;
    }
    return true;
  }
}
