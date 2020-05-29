package io.github.fukkitmc.fukkit.threads;

public class MixinThreadWrapper extends Thread{

    public Runnable code;

    public MixinThreadWrapper(Runnable runnable){
        this.code = runnable;
    }

    @Override
    public void run() {
        code.run();
    }
}
