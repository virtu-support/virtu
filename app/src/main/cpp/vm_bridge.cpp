#include <jni.h>
#include <android/log.h>
#include <unistd.h>
#include <sys/wait.h>

#define LOG_TAG "VirtuJNI"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)

extern "C" JNIEXPORT jint JNICALL
Java_org_virtu_android_VmEngine_runCommand(JNIEnv *env, jobject thiz, jstring command) {
    const char *cmd = env->GetStringUTFChars(command, nullptr);
    LOGI("Running command: %s", cmd);

    pid_t pid = fork();
    if (pid == 0) {
        execl("/system/bin/sh", "sh", "-c", cmd, nullptr);
        return -1;
    } else if (pid > 0) {
        int status;
        waitpid(pid, &status, 0);
        env->ReleaseStringUTFChars(command, cmd);
        return status;
    } else {
        env->ReleaseStringUTFChars(command, cmd);
        return -2;
    }
}
