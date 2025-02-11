define double @calcularMedia([5 x double]* %notas.param) {
    %notas = alloca [5 x double]*
    store [5 x double]* %notas.param, [5 x double]** %notas
    %i = alloca i32
    %soma = alloca double
    %1 = sitofp i32 0 to double
    store double %1, double* %soma
    store i32 0, i32* %i
    br label %..for.head0
    ..for.head0:
    %2 = load i32, i32* %i
    %3 = icmp slt i32 %2, 5
    br i1 %3, label %..for.body0, label %..for.end0
    ..for.body0:
    %4 = load double, double* %soma
    %5 = load [5 x double]*, [5 x double]** %notas
    %6 = load i32, i32* %i
    %7 = getelementptr inbounds [5 x double], [5 x double]* %5, i32 0, i32 %6
    %8 = load double, double* %7
    %9 = fadd double %4, %8
    store double %9, double* %soma
    %10 = load i32, i32* %i
    %11 = add i32 %10, 1
    store i32 %11, i32* %i
    br label %..for.head0
    ..for.end0:
    %12 = load double, double* %soma
    %13 = sitofp i32 5 to double
    %14 = fdiv double %12, %13
    ret double %14
}
define i32 @main() {
    %notas = alloca [5 x double]*
    %1 = alloca [5 x double]
    store [5 x double]* %1, [5 x double]** %notas
    %media = alloca double
    %i = alloca i32
    store i32 0, i32* %i
    br label %..for.head1
    ..for.head1:
    %2 = load i32, i32* %i
    %3 = icmp slt i32 %2, 5
    br i1 %3, label %..for.body1, label %..for.end1
    ..for.body1:
    %nota = alloca double
    %4 = load i32, i32* %i
    %5 = add i32 %4, 1
    call i32 (i8*, ...) @printf(i8* @.str0, i32 %5)
    call i32 (i8*, ...) @scanf(i8* @.str1, double* %nota)
    %8 = load [5 x double]*, [5 x double]** %notas
    %9 = load i32, i32* %i
    %10 = getelementptr inbounds [5 x double], [5 x double]* %8, i32 0, i32 %9
    %11 = load double, double* %nota
    store double %11, double* %10
    %12 = load i32, i32* %i
    %13 = add i32 %12, 1
    store i32 %13, i32* %i
    br label %..for.head1
    ..for.end1:
    %14 = load [5 x double]*, [5 x double]** %notas
    %15 = call double @calcularMedia([5 x double]* %14)
    store double %15, double* %media
    %16 = load double, double* %media
    call i32 (i8*, ...) @printf(i8* @.str2, double %16)
    %18 = load double, double* %media
    %19 = sitofp i32 7 to double
    %20 = fcmp olt double %18, %19
    br i1 %20, label %..if0, label %..else0
    ..if0:
    call i32 (i8*, ...) @printf(i8* @.str3)
    br label %..if.end0
    ..else0:
    call i32 (i8*, ...) @printf(i8* @.str4)
    br label %..if.end0
    ..if.end0:
    ret i32 0
}
@.str0 = private constant [28 x i8] c"Digite a %d nota do aluno:\0A\00"
@.str1 = private constant [4 x i8] c"%lf\00"
@.str2 = private constant [29 x i8] c"A m\C3\A9dia do aluno foi: %.1f\0A\00"
@.str3 = private constant [33 x i8] c"O aluno n\C3\A3o atingiu a m\C3\A9dia 7\0A\00"
@.str4 = private constant [28 x i8] c"O aluno atingiu a m\C3\A9dia 7\0A\00"
declare i32 @printf(i8*, ...)
declare i32 @scanf(i8*, ...)
