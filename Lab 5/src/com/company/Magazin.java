package com.company;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Bogdan95 on 11/30/2016.
 */
public class Magazin {
    private volatile int sold_total;
    private int nr_clienti;

    private ExecutorService executor = Executors.newFixedThreadPool(4);
    private ScheduledExecutorService scheduledExecutor = Executors.newScheduledThreadPool(4);
    private ConcurrentMap<Integer, Produs> produse = new ConcurrentHashMap<>(); // lista produse disponibile
    private ConcurrentLinkedQueue<Factura> facturi = new ConcurrentLinkedQueue<>(); // queue facturilor generate

    private final static String path_lista_produse = System.getProperty("user.dir") + "\\src\\" + "lista_produse.txt";
    private final static String path_lista_facturi = System.getProperty("user.dir") + "\\src\\" + "lista_facturi.txt";
    private final static String path_operatii = System.getProperty("user.dir") + "\\src\\" + "operatii.txt";


    public Magazin(int sold_total, int nr_clienti) {
        this.sold_total = sold_total;
        this.nr_clienti = nr_clienti;
    }

    public void open() {
        try {
            Random rand = new Random();
            initiereListaProduse(path_lista_produse);
            scheduledExecutor.scheduleAtFixedRate(operationsChecking(), 0, 5, TimeUnit.SECONDS);
            for (int i = 0; i < nr_clienti; i++) {
                Future f = executor.submit(new Runnable() {
                    @Override
                    public void run() {
                        scheduledExecutor.scheduleAtFixedRate(generateSale(), rand.nextInt(3), 5, TimeUnit.SECONDS);
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Runnable generateSale() {
        return new Runnable() {
            @Override
            public void run() {
                CompletableFuture<Factura> promise = CompletableFuture.supplyAsync(() -> {
                    return generateFactura();
                });
                promise.thenAccept(factura -> {
                    try {
                        writeFacturaToFile(path_lista_facturi, factura);
                        System.out.println("Generated a bill...");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        };
    }

    private synchronized Factura generateFactura() {
        Random rand = new Random();
        Factura factura = new Factura();
        int nr_articole = rand.nextInt(3) + 1;

        for (int i = 0; i < nr_articole; i++) {
            int cantitate = rand.nextInt(10) + 1;
            int articol_nr = rand.nextInt(produse.size()) + 1;
            Produs p = produse.get(articol_nr);
            Vanzare v = new Vanzare(p, cantitate);

            if (p.getStoc() - cantitate >= 0) {
                p.setStoc(p.getStoc() - cantitate);
                factura.addProdus(v);
                produse.put(articol_nr, p);
            }
        }
        facturi.add(factura);
        sold_total += factura.getSuma_totala();
        return factura;
    }

    private Runnable operationsChecking() {
        return new Runnable() {
            @Override
            public void run() {
                CompletableFuture<List<String>> promise = CompletableFuture.supplyAsync(() -> {
                    return systemCheck();
                });
                promise.thenAccept(lista -> {

                    try {
                        writeOperationsToFile(path_operatii, lista);
                        System.out.println("System check Done...");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

            }
        };
    }

    private List<String> systemCheck() {
        System.out.println("Started system check...");
        List<String> list = new LinkedList<>();
        synchronized (facturi) {
            // 0. Time
            Calendar c = new GregorianCalendar();
            list.add(getDateAsString(c));
            // 1. Stoc check
            list.add("Stock:");
            produse.forEach((key, product) -> {
                String s1 = product.getNume() + ":" + product.getStoc();
                list.add(s1);
            });
            int nr_of_products = 0;
            int income = 0;
            for (Factura factura : facturi) {
                for (Vanzare vanzare : factura.getLista_produse()) {
                    // 2. Total sold products
                    nr_of_products += vanzare.getCantitate();
                }
                // 3. Income
                income += factura.getSuma_totala();
            }
            String s2 = "Nr of sold products:" + nr_of_products;
            list.add(s2);
            String s3 = "Income: " + income;
            list.add(s3);
            // 4. Total Income
            String s4 = "Total income: " + sold_total;
            list.add(s4);
            list.add("\n");
            // 5. Clean facturi queue
            facturi.clear();
        }
        return list;
    }

    private void initiereListaProduse(String path) throws IOException {
        FileReader fr = new FileReader(path);
        BufferedReader br = new BufferedReader(fr);
        String line;
        int counter = 0;
        line = br.readLine();
        while (line != null) {
            String[] tokens = line.split(" ");
            String name = tokens[0];
            int code = Integer.parseInt(tokens[1]);
            int pret_unitar = Integer.parseInt(tokens[2]);
            String unit_masura = tokens[3];
            int stoc = Integer.parseInt(tokens[4]);
            Produs p = new Produs(name, code, pret_unitar, unit_masura, stoc);
            produse.put(++counter, p);
            line = br.readLine();
        }
        br.close();
        fr.close();
    }

    private synchronized void writeFacturaToFile(String path, Factura factura) throws IOException {
        FileWriter fw = new FileWriter(path, true);
        PrintWriter print_line = new PrintWriter(fw);

        Calendar data = factura.getData();
        String data_str = data.get(Calendar.DAY_OF_MONTH) + "/" + data.get(Calendar.MONTH) + "/" +
                data.get(Calendar.YEAR) + " " + data.get(Calendar.HOUR) + ":" + data.get(Calendar.MINUTE) + ":" +
                data.get(Calendar.SECOND);

        print_line.println(data_str);

        List<Vanzare> produse = factura.getLista_produse();
        for (Vanzare vanzare : produse) {
            String item;
            int pret_total_produs = vanzare.getProdus().getPret_unitar() * vanzare.getCantitate();
            item = vanzare.getProdus().getNume() + " " + vanzare.getProdus().getCod() + " "
                    + vanzare.getProdus().getUnit_masura() + " " + vanzare.getProdus().getPret_unitar() + " "
                    + vanzare.getCantitate() + " " + pret_total_produs;
            print_line.println(item);
        }
        print_line.println(factura.getSuma_totala());
        print_line.println();

        print_line.close();
        fw.close();
    }

    private synchronized void writeOperationsToFile(String path, List<String> messages) throws IOException {
        FileWriter fw = new FileWriter(path, true);
        PrintWriter print_line = new PrintWriter(fw);

        for (String message : messages) {
            print_line.println(message);
        }

        print_line.close();
        fw.close();
    }

    public static String getDateAsString(Calendar data) {
        return data.get(Calendar.DAY_OF_MONTH) + "/" + data.get(Calendar.MONTH) + "/" +
                data.get(Calendar.YEAR) + " " + data.get(Calendar.HOUR) + ":" + data.get(Calendar.MINUTE) + ":" +
                data.get(Calendar.SECOND);
    }
}