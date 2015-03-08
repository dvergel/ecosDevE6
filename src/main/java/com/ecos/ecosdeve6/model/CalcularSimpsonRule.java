/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ecos.ecosdeve6.model;

import com.ecos.ecosdeve6.exceptions.ExceptionApp;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * CalcularSimpsonRule
 *
 * calcula las variables nesesarias para integrar una función de distribución
 * estadística simétrica sobre un rango especificado
 *
 * @author Dev
 * @version 1.0
 * @since 1.0
 */
public class CalcularSimpsonRule {

    private List<BigDecimal> list = new ArrayList<BigDecimal>();
    private BigDecimal w = BigDecimal.ZERO;
    private BigDecimal e = BigDecimal.ZERO;
    private BigDecimal num_seg = BigDecimal.ZERO;
    private BigDecimal dof = BigDecimal.ZERO;
    private BigDecimal x = BigDecimal.ZERO;
    private BigDecimal p = BigDecimal.ZERO;
    private BigDecimal resultado = BigDecimal.ZERO;
    private BigDecimal fXestatico = BigDecimal.ZERO;
    private List<BigDecimal> fX = new ArrayList<BigDecimal>();
    private List<CalcularSimpsonRule> intentos = new ArrayList<CalcularSimpsonRule>();
    private BigDecimal proyectarP = BigDecimal.ZERO;
    private BigDecimal auxiliar = BigDecimal.ZERO;
    private BigDecimal d = new BigDecimal(0.5);
    private boolean flag = false;

    /**
     * CalcularSimpsonRule
     *
     * contructor de la clase y carga de variables de inicio para los calculos
     * del metodos Simpson
     *
     * @param e
     * @param x
     * @param num_seg
     * @param dof
     */
    public CalcularSimpsonRule(BigDecimal e, BigDecimal x, BigDecimal num_seg, BigDecimal dof) {
        this.w = x.divide(num_seg, MathContext.DECIMAL64);
        this.x = x;
        this.e = e;
        this.num_seg = num_seg;
        this.dof = dof;
        this.intentos.clear();
        this.fX.clear();
        this.list.clear();
    }

    /**
     * CalcularSimpsonRule
     *
     * contructor de la clase y carga de variables de inicio para los calculos
     * del metodos Simpson con proyeccion de p
     *
     * @param e
     * @param x
     * @param num_seg
     * @param dof
     * @param p  
     */
    public CalcularSimpsonRule(BigDecimal e, BigDecimal x, BigDecimal num_seg, BigDecimal dof, BigDecimal p) {
        this.proyectarP = p;
        //this.w = x.divide(num_seg, MathContext.DECIMAL64).setScale(5, RoundingMode.HALF_UP);
        this.x = x;
        this.e = e;
        this.num_seg = num_seg;
        this.dof = dof;
        this.intentos.clear();
        this.fX.clear();
        this.list.clear();
    }

    /**
     * calcularP
     *
     * calcula la integral de la funcion que sera evaluada posteriormente para
     * la verificacion de cumplimiento de criterio de tolerancia de desviacion
     *
     * @throws Exception
     */
    public void calcularP() throws Exception {
        try {
            calcularLista();
            calcularFdeX();
            BigDecimal formula1 = calcularSumaVector(0, 1, new BigDecimal(1));
            BigDecimal formula2 = calcularSumaVector(1, getNum_seg().intValue(), new BigDecimal(4));
            BigDecimal formula3 = calcularSumaVector(2, getNum_seg().subtract(new BigDecimal(1)).intValue(), new BigDecimal(2));
            BigDecimal formula4 = calcularSumaVector(getNum_seg().intValue(), getNum_seg().add(new BigDecimal(1)).intValue(), new BigDecimal(1));
            setP(formula1.add(formula2).add(formula3).add(formula4));
        } catch (Exception ex) {
            throw new ExceptionApp("Error al Calcular P :" + ex.getMessage());
        }
    }

    /**
     * calcularLista
     *
     * calcula el valor de los datos de prueba con los intervalos de el atributo
     * w
     *
     */
    public void calcularLista() {
        setW(x.divide(num_seg, MathContext.DECIMAL64));//m
        getList().add(BigDecimal.ZERO);
        BigDecimal acumulador = getW();
        for (int index = 1; index <= num_seg.intValue(); index++) {
            getList().add(acumulador);
            acumulador = acumulador.add(getW());//m
        }
    }

    /**
     * calcular
     *
     * integra todos los calculos nesesarios para comprobar el metodo Simpson
     *
     * @throws Exception
     */
    public void calcular() throws Exception {
        try {
            CalcularSimpsonRule intento = new CalcularSimpsonRule(this.e, this.x, this.num_seg, this.dof);
            BigDecimal acumulador_num_seg = BigDecimal.ZERO;
            intento.calcularP();
            getIntentos().add(intento);
            do {
                acumulador_num_seg = acumulador_num_seg.add(this.num_seg.multiply(new BigDecimal(2)));
                intento = new CalcularSimpsonRule(this.e, this.x, acumulador_num_seg, this.dof);
                intento.calcularP();
                getIntentos().add(intento);
            } while (intentos.get(intentos.size() - 2).getP().subtract(intentos.get(intentos.size() - 1).getP()).abs().setScale(5, RoundingMode.HALF_UP).compareTo(getE().setScale(5, RoundingMode.HALF_UP)) >= 0);
            setResultado(intentos.get(intentos.size() - 1).getP());
        } catch (Exception ex) {
            throw new ExceptionApp("Error al Calcular la variables:" + ex.getMessage());
        }
    }

    /**
     * calcularSumaVector
     *
     * calcula la suma de los rangos seleccionados aumentando 2 posiciones y
     * multiplicando por el multiplicador las posiciones del vector de f(x)
     * enviadas en los parametros inicio y fin
     *
     * @param inicio
     * @param fin
     * @param multiplo
     * @return
     * @throws Exception
     */
    public BigDecimal calcularSumaVector(int inicio, int fin, BigDecimal multiplo) throws Exception {
        try {
            BigDecimal acumulador = BigDecimal.ZERO;
            for (int index = inicio; index < fin; index = index + 2) {
                acumulador = acumulador.add(getW().divide(new BigDecimal(3), MathContext.DECIMAL64).multiply(multiplo.multiply(getfX().get(index))));
            }
            return acumulador;
        } catch (Exception ex) {
            throw new ExceptionApp("Error al Calcular la suma del vector :" + ex.getMessage());
        }
    }

    /**
     * calcularFdeXEstatico
     *
     * calcula el valor estatico de la funcion f(x) nesesario para calcular p
     *
     * @throws Exception
     */
    public void calcularFdeXEstatico() throws Exception {
        try {
            BigDecimal formula1 = getDof().add(BigDecimal.ONE).divide(new BigDecimal(2), MathContext.DECIMAL64);
            BigDecimal formula2 = new BigDecimal(Math.sqrt(getDof().multiply(new BigDecimal(Math.PI)).doubleValue()));
            BigDecimal formula3 = getDof().divide(new BigDecimal(2), MathContext.DECIMAL64);
            if (formula1.doubleValue() % 1 == 0) {
                formula1 = calcularGammaEntero(formula1);
            } else {
                formula1 = calcularGammaDecimal(formula1);
            }
            if (formula3.doubleValue() % 1 == 0) {
                formula3 = calcularGammaEntero(formula3);
            } else {
                formula3 = calcularGammaDecimal(formula3);
            }
            setfXestatico(formula1.divide(formula2.multiply(formula3), MathContext.DECIMAL64));
        } catch (Exception ex) {
            throw new ExceptionApp("Error al Calcular el valor estatico de f(x) :" + ex.getMessage());
        }
    }

    /**
     * calcularFdeX
     *
     * calcula f(x) para todos los valores del la lista de valores de prueba
     *
     * @throws Exception
     */
    public void calcularFdeX() throws Exception {
        try {
            calcularFdeXEstatico();
            BigDecimal formula1;
            BigDecimal formula2;
            for (BigDecimal x : getList()) {
                formula1 = BigDecimal.ONE.add(x.pow(2).divide(dof, MathContext.DECIMAL64));
                formula2 = getDof().add(BigDecimal.ONE).divide(new BigDecimal(2), MathContext.DECIMAL64).multiply(new BigDecimal(-1));
                getfX().add(new BigDecimal(Math.pow(formula1.doubleValue(), formula2.doubleValue())).multiply(getfXestatico()));
            }
        } catch (Exception ex) {
            throw new ExceptionApp("Error al Calcular f(x) :" + ex.getMessage());
        }
    }

    /**
     * calcularGammaEntero
     *
     * calcula el valor de gamma para los valores enteros con la funcion r(x-1)!
     *
     * @param xi
     * @return
     * @throws Exception
     */
    public BigDecimal calcularGammaEntero(BigDecimal xi) throws Exception {
        try {
            BigDecimal gamma = BigDecimal.ONE;
            for (int index = 2; index <= xi.intValue() - 1; index++) {
                gamma = gamma.multiply(new BigDecimal(index));
            }
            return gamma;
        } catch (Exception ex) {
            throw new ExceptionApp("Error al Calcular el valor de gamma cuando es un entero :" + ex.getMessage());
        }
    }

    /**
     * calcularGammaDecimal
     *
     * calcular el valor de gamma para numeros decimales restando la una unidad
     * y multiplicando los resultados hasta llegar a 0.5 para remplazar por raiz
     * de PI y multiplicarlo con el acumulado
     *
     * @param xi
     * @return
     * @throws Exception
     */
    public BigDecimal calcularGammaDecimal(BigDecimal xi) throws Exception {
        try {
            BigDecimal gamma = BigDecimal.ONE;
            BigDecimal xiMultiplo = xi;
            do {
                xiMultiplo = xiMultiplo.subtract(new BigDecimal(1));
                gamma = gamma.multiply(xiMultiplo);
            } while (xiMultiplo.subtract(new BigDecimal(1)).compareTo(new BigDecimal(0)) >= 0);
            gamma = gamma.multiply(new BigDecimal(Math.sqrt(Math.PI)));
            return gamma;
        } catch (Exception ex) {
            throw new ExceptionApp("Error al Calcular el valor de gamma cuando es un decimal :" + ex.getMessage());
        }
    }

    /**
     * proyectarX
     * 
     * proyecta el valor de x hasta obtener el valor deseado de p
     * 
     * @throws Exception
     */
    public void proyectarX() throws Exception {
        try {
            CalcularSimpsonRule intento = new CalcularSimpsonRule(this.e, this.x, this.num_seg, this.dof);
            BigDecimal acumulador_num_seg = BigDecimal.ZERO;
            int signo;
            intento.calcularP();
            signo = intento.getP().setScale(5, RoundingMode.HALF_UP).compareTo(getProyectarP().setScale(5, RoundingMode.HALF_UP));
            getIntentos().add(intento);
            do {
                acumulador_num_seg = acumulador_num_seg.add(this.num_seg.multiply(new BigDecimal(2)));
                if (!getProyectarP().equals(BigDecimal.ZERO) && getIntentos().size() >= 2) {
                    setAuxiliar(getResultado());
                    if ((intento.getP().setScale(5, RoundingMode.HALF_UP).compareTo(getProyectarP().setScale(5, RoundingMode.HALF_UP)) < 0) && !intento.getP().setScale(5, RoundingMode.HALF_UP).equals(getAuxiliar().setScale(5, RoundingMode.HALF_UP))) {
                        setX(getX().add(getD()));
                    } else if ((intento.getP().setScale(5, RoundingMode.HALF_UP).compareTo(getProyectarP().setScale(5, RoundingMode.HALF_UP)) > 0) && !intento.getP().setScale(5, RoundingMode.HALF_UP).equals(getAuxiliar().setScale(5, RoundingMode.HALF_UP))) {
                        setX(getX().subtract(getD()));
                    }
                }
                intento = new CalcularSimpsonRule(this.e, this.x, this.num_seg.multiply(new BigDecimal(2)), this.dof);
                intento.calcularP();
                if (signo != intento.getP().setScale(5, RoundingMode.HALF_UP).compareTo(getProyectarP().setScale(5, RoundingMode.HALF_UP))) {
                    setD(d.divide(new BigDecimal(2), MathContext.DECIMAL64));
                }
                signo = intento.getP().setScale(5, RoundingMode.HALF_UP).compareTo(getProyectarP().setScale(5, RoundingMode.HALF_UP));
                if (intento.getP().setScale(5, RoundingMode.HALF_UP).equals(getProyectarP().setScale(5, RoundingMode.HALF_UP))) {
                    setFlag(true);
                }
                getIntentos().add(intento);
            } while ((intentos.get(intentos.size() - 2).getP().setScale(5, RoundingMode.HALF_UP).subtract(intentos.get(intentos.size() - 1).getP().setScale(5, RoundingMode.HALF_UP)).abs().setScale(5, RoundingMode.HALF_UP).compareTo(getE().setScale(5, RoundingMode.HALF_UP)) >= 0) || (!isFlag()));
            setResultado(intentos.get(intentos.size() - 1).getP().setScale(5, RoundingMode.HALF_UP));
        } catch (Exception ex) {
            throw new ExceptionApp("Error al Calcular la variables:" + ex.getMessage());
        }
    }

    /**
     *
     * @return
     */
    public BigDecimal getP() {
        return p;
    }

    /**
     *
     * @param p
     */
    public void setP(BigDecimal p) {
        this.p = p;
    }

    /**
     *
     * @return
     */
    public List<BigDecimal> getList() {
        return list;
    }

    /**
     *
     * @param list
     */
    public void setList(List<BigDecimal> list) {
        this.list = list;
    }

    /**
     *
     * @return
     */
    public BigDecimal getW() {
        return w;
    }

    /**
     *
     * @param w
     */
    public void setW(BigDecimal w) {
        this.w = w;
    }

    /**
     *
     * @return
     */
    public BigDecimal getE() {
        return e;
    }

    /**
     *
     * @param e
     */
    public void setE(BigDecimal e) {
        this.e = e;
    }

    /**
     *
     * @return
     */
    public BigDecimal getDof() {
        return dof;
    }

    /**
     *
     * @param dof
     */
    public void setDof(BigDecimal dof) {
        this.dof = dof;
    }

    /**
     *
     * @return
     */
    public BigDecimal getX() {
        return x;
    }

    /**
     *
     * @param x
     */
    public void setX(BigDecimal x) {
        this.x = x;
    }

    /**
     *
     * @return
     */
    public BigDecimal getfXestatico() {
        return fXestatico;
    }

    /**
     *
     * @param fXestatico
     */
    public void setfXestatico(BigDecimal fXestatico) {
        this.fXestatico = fXestatico;
    }

    /**
     *
     * @return
     */
    public List<BigDecimal> getfX() {
        return fX;
    }

    /**
     *
     * @param fX
     */
    public void setfX(List<BigDecimal> fX) {
        this.fX = fX;
    }

    /**
     *
     * @return
     */
    public BigDecimal getNum_seg() {
        return num_seg;
    }

    /**
     *
     * @param num_seg
     */
    public void setNum_seg(BigDecimal num_seg) {
        this.num_seg = num_seg;
    }

    /**
     *
     * @return
     */
    public List<CalcularSimpsonRule> getIntentos() {
        return intentos;
    }

    /**
     *
     * @param intentos
     */
    public void setIntentos(List<CalcularSimpsonRule> intentos) {
        this.intentos = intentos;
    }

    /**
     *
     * @return
     */
    public BigDecimal getResultado() {
        return resultado;
    }

    /**
     *
     * @param resultado
     */
    public void setResultado(BigDecimal resultado) {
        this.resultado = resultado;
    }

    /**
     *
     * @return
     */
    public BigDecimal getProyectarP() {
        return proyectarP;
    }

    /**
     *
     * @param proyectarP
     */
    public void setProyectarP(BigDecimal proyectarP) {
        this.proyectarP = proyectarP;
    }

    /**
     *
     * @return
     */
    public BigDecimal getAuxiliar() {
        return auxiliar;
    }

    /**
     *
     * @param auxiliar
     */
    public void setAuxiliar(BigDecimal auxiliar) {
        this.auxiliar = auxiliar;
    }

    /**
     *
     * @return
     */
    public BigDecimal getD() {
        return d;
    }

    /**
     *
     * @param d
     */
    public void setD(BigDecimal d) {
        this.d = d;
    }

    /**
     *
     * @return
     */
    public boolean isFlag() {
        return flag;
    }

    /**
     *
     * @param flag
     */
    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + (this.list != null ? this.list.hashCode() : 0);
        hash = 53 * hash + (this.w != null ? this.w.hashCode() : 0);
        hash = 53 * hash + (this.e != null ? this.e.hashCode() : 0);
        hash = 53 * hash + (this.num_seg != null ? this.num_seg.hashCode() : 0);
        hash = 53 * hash + (this.dof != null ? this.dof.hashCode() : 0);
        hash = 53 * hash + (this.x != null ? this.x.hashCode() : 0);
        hash = 53 * hash + (this.p != null ? this.p.hashCode() : 0);
        hash = 53 * hash + (this.resultado != null ? this.resultado.hashCode() : 0);
        hash = 53 * hash + (this.fXestatico != null ? this.fXestatico.hashCode() : 0);
        hash = 53 * hash + (this.fX != null ? this.fX.hashCode() : 0);
        hash = 53 * hash + (this.intentos != null ? this.intentos.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CalcularSimpsonRule other = (CalcularSimpsonRule) obj;
        if (this.list != other.list && (this.list == null || !this.list.equals(other.list))) {
            return false;
        }
        if (this.w != other.w && (this.w == null || !this.w.equals(other.w))) {
            return false;
        }
        if (this.e != other.e && (this.e == null || !this.e.equals(other.e))) {
            return false;
        }
        if (this.num_seg != other.num_seg && (this.num_seg == null || !this.num_seg.equals(other.num_seg))) {
            return false;
        }
        if (this.dof != other.dof && (this.dof == null || !this.dof.equals(other.dof))) {
            return false;
        }
        if (this.x != other.x && (this.x == null || !this.x.equals(other.x))) {
            return false;
        }
        if (this.p != other.p && (this.p == null || !this.p.equals(other.p))) {
            return false;
        }
        if (this.resultado != other.resultado && (this.resultado == null || !this.resultado.equals(other.resultado))) {
            return false;
        }
        if (this.fXestatico != other.fXestatico && (this.fXestatico == null || !this.fXestatico.equals(other.fXestatico))) {
            return false;
        }
        if (this.fX != other.fX && (this.fX == null || !this.fX.equals(other.fX))) {
            return false;
        }
        if (this.intentos != other.intentos && (this.intentos == null || !this.intentos.equals(other.intentos))) {
            return false;
        }
        return true;
    }
}
