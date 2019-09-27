#include <stdio.h>
#include <stdlib.h>
#include <signal.h>
#include <unistd.h>

#ifdef CONIO_H
#include <conio.h>
#endif

#define LOCK "fts.lock"

void init();
void run();
void onClose();
void handleSignal(int);
void holdScreen();

int main() {
    init();
    run();
    return 0;
}

void init() {
#ifdef _WIN32
    // set window title
    system("title File Transfer System");
#else
    // set window title
    system("echo \"\033]0;File Transfer System\a\"");
#endif

    // allow only one instance
    if (access(LOCK, F_OK) != -1) {
        // display error message
        printf("Another instance is already running!\nOnly one instance is allowed.");

        // hold the screen
        holdScreen();

        // return exit code 1
        exit(1);
    } else {
        // create lock
        FILE *fptr = fopen(LOCK, "w");
        fclose(fptr);
    }

    // set up signals
    signal(SIGINT, handleSignal);
#ifdef SIGHUP
    signal(SIGHUP, handleSignal);
#endif
#ifdef SIGBREAK
    signal(SIGBREAK, handleSignal);
#endif
}

void run() {
    // display IP address
    printf("--------------------------------------------------------------------------------\n");
    system("ipconfig | findstr IPv4");
    printf("--------------------------------------------------------------------------------\n\n");

#ifdef _WIN32
    // execute application
    int code = system("java-runtime\\bin\\java -jar fts-0.0.1-SNAPSHOT.war");
#else
    // execute application
    int code = system("java-runtime/bin/java -jar fts-0.0.1-SNAPSHOT.war");
#endif

    // delete locks
    onClose();

#ifdef WTERMSIG
    if (!WTERMSIG(code)) {
        // hold the screen
        holdScreen();
    }
#else
    // hold the screen
    holdScreen();
#endif
}

void onClose() {
    // remove the lock
    remove(LOCK);
}

void handleSignal(int signal) {
    // if interrupted, start closing event
    onClose();
}

void holdScreen() {
#ifdef CONIO_H
    // display message and wait for any key
    printf("\n\nPress any key to exit ...");
    getch();
#else
    // display message and wait for ENTER key
    printf("\n\nPress ENTER to exit ...");
    getchar();
#endif
}
